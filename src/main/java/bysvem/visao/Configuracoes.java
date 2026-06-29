package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Operador;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class Configuracoes extends JDialog {

    private Conta usuarioLogado;

    public Configuracoes(JFrame parent, Conta conta) {
        super(parent, "Configurações", true);
        this.usuarioLogado = conta;

        setSize(1000, 800);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVerPerfil = new JButton("Ver Perfil");
        JButton btnAlterarNome = new JButton("Alterar Nome");
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        JButton btnAlterarEmail = new JButton("Alterar E-mail");
        JButton btnAdicionarSaldo = new JButton("Adicionar Saldo");
        JButton btnTornarDev = new JButton("Tornar-se Desenvolvedor");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.PLAIN, 35);
        for (JButton b : new JButton[]{btnVerPerfil, btnAlterarNome, btnAlterarSenha, btnAlterarEmail, btnAdicionarSaldo, btnTornarDev, btnVoltar}) {
            b.setFont(fonte);
        }

        btnVerPerfil.addActionListener(e -> verPerfil());
        btnAlterarNome.addActionListener(e -> alterarNome());
        btnAlterarSenha.addActionListener(e -> alterarSenha());
        btnAlterarEmail.addActionListener(e -> alterarEmail());
        btnAdicionarSaldo.addActionListener(e -> adicionarSaldo()); 
        btnTornarDev.addActionListener(e -> tornarDesenvolvedor());
        btnVoltar.addActionListener(e -> dispose());

        if (conta instanceof Desenvolvedor || conta instanceof Operador) {
            btnTornarDev.setEnabled(false);
            btnTornarDev.setText("Já é Desenvolvedor/Operador");
        }

        if (!(conta instanceof Usuario)) {
            btnAdicionarSaldo.setVisible(false);
        }

        panel.add(btnVerPerfil);
        panel.add(btnAlterarNome);
        panel.add(btnAlterarSenha);
        panel.add(btnAlterarEmail);
        panel.add(btnAdicionarSaldo);
        panel.add(btnTornarDev);
        panel.add(btnVoltar);

        add(panel);
        setVisible(true);
    }


    private void verPerfil() {
        String tipo;
        if (usuarioLogado instanceof Desenvolvedor) {
            Desenvolvedor dev = (Desenvolvedor) usuarioLogado;
            tipo = "Desenvolvedor<br>Empresa: " + dev.getEmpresa();
        } else if (usuarioLogado instanceof Operador) {
            tipo = "Operador";
        } else {
            tipo = "Usuário";
        }

        String mensagem = String.format(
            "<html>" +
            "<div style='text-align: center; font-family: monospace; font-size: 35px; font-weight: bold;'>" +
            " PERFIL </div>" +
            "<div style='text-align: left; font-family: monospace; font-size: 28px; padding-left: 15px;'>" +
            "<b>ID:</b> %d<br>" +
            "<b>Nome:</b> %s<br>" +
            "<b>E-mail:</b> %s<br>" +
            "<b>Senha:</b> %d<br>" +
            "<b>Tipo:</b> %s<br>" +
            "<b>Banido:</b> %s" +
            "</div></html>",
            usuarioLogado.getId(),
            usuarioLogado.getNome(),
            usuarioLogado.getEmail(),
            usuarioLogado.getSenha(),
            tipo,
            usuarioLogado.getBan() ? "Sim" : "Não"
        );

        JLabel label = new JLabel(mensagem);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.setPreferredSize(new Dimension(700, 400));

        JOptionPane.showMessageDialog(this,
                panel,
                "Meu Perfil",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void alterarNome() {
        String novoNome = JOptionPane.showInputDialog(this,
                "Digite o novo nome (ou deixe em branco para cancelar):",
                "Alterar Nome", JOptionPane.QUESTION_MESSAGE);
        if (novoNome == null || novoNome.trim().isEmpty()) return;

        try {
            usuarioLogado.atualizarNome(novoNome.trim());
            JOptionPane.showMessageDialog(this, "Nome alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Falha ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarSenha() {
        String senhaAtualStr = JOptionPane.showInputDialog(this,
                "Digite a senha atual (ou cancelar para abortar):",
                "Alterar Senha", JOptionPane.QUESTION_MESSAGE);
        if (senhaAtualStr == null) return;

        int senhaAtual;
        try {
            senhaAtual = Integer.parseInt(senhaAtualStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A senha deve ser numérica.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novaSenhaStr = JOptionPane.showInputDialog(this,
                "Digite a nova senha (apenas números):",
                "Alterar Senha", JOptionPane.QUESTION_MESSAGE);
        if (novaSenhaStr == null || novaSenhaStr.trim().isEmpty()) return;

        int novaSenha;
        try {
            novaSenha = Integer.parseInt(novaSenhaStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A nova senha deve ser numérica.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            usuarioLogado.atualizarSenha(senhaAtual, novaSenha);
            JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Falha ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarEmail() {
        String novoEmail = JOptionPane.showInputDialog(this,
                "Digite o novo e-mail:", "Alterar E-mail", JOptionPane.QUESTION_MESSAGE);
        if (novoEmail == null || novoEmail.trim().isEmpty()) return;

        try {
            usuarioLogado.atualizarEmail(novoEmail.trim());
            JOptionPane.showMessageDialog(this, "E-mail alterado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Falha ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarSaldo() {
        if (!(usuarioLogado instanceof Usuario)) {
            JOptionPane.showMessageDialog(this,
                    "Esta opção está disponível apenas para usuários comuns.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usuario = (Usuario) usuarioLogado;

        String valorStr = JOptionPane.showInputDialog(this,
                "Digite o valor a ser adicionado ao saldo (R$):\nSaldo atual: R$" + String.format("%.2f", usuario.getSaldo()),
                "Adicionar Saldo", JOptionPane.QUESTION_MESSAGE);

        if (valorStr == null || valorStr.trim().isEmpty()) {
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Valor inválido. Digite um número (use ponto para decimais).",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirma a adição de R$" + String.format("%.2f", valor) + " ao saldo?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            usuario.adicionarSaldo(valor);
            JOptionPane.showMessageDialog(this,
                    "Saldo atualizado com sucesso!\nNovo saldo: R$" + String.format("%.2f", usuario.getSaldo()),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this,
                    "Falha ao salvar as alterações: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tornarDesenvolvedor() {
        if (usuarioLogado instanceof Desenvolvedor) {
            JOptionPane.showMessageDialog(this, "Você já é um desenvolvedor.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (usuarioLogado instanceof Operador) {
            JOptionPane.showMessageDialog(this, "Operadores não podem se tornar desenvolvedores.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String empresa = JOptionPane.showInputDialog(this,
                "Digite o nome da sua empresa:",
                "Tornar-se Desenvolvedor", JOptionPane.QUESTION_MESSAGE);
        if (empresa == null || empresa.trim().isEmpty()) {
            return;
        }

        try {
            Desenvolvedor dev = usuarioLogado.promoverParaDesenvolvedor(empresa.trim());
            usuarioLogado = dev;

            JOptionPane.showMessageDialog(this,
                    "Conta promovida a Desenvolvedor! Você será redirecionado para a tela de login.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            java.awt.Window parent = getOwner();
            if (parent != null) {
                parent.dispose();
            }
            SwingUtilities.invokeLater(() -> new TelaLogin());

        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao promover conta: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean salvarAlteracoes(){

        try{
            EntidadeDAO<Conta> dao = GerenciadorPersistencia.getInstancia().getDAO(Conta.class);
            dao.atualizar(usuarioLogado);
            dao.persistir("dados/contas.dat");
            return true;

        }catch(PersistenceException e){
            e.printStackTrace();
            return false;
        }
    }
}