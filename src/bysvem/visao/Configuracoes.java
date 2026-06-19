package bysvem.visao;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Gerenciador;
import bysvem.modelo.Operador;

public class Configuracoes extends JDialog {

    private Conta usuarioLogado;

    public Configuracoes(JFrame parent, Conta conta) {
        super(parent, "Configurações", true);
        this.usuarioLogado = conta;

        setSize(500, 500); // Aumentei para caber mais um botão
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnVerPerfil = new JButton("Ver Perfil");
        JButton btnAlterarNome = new JButton("Alterar Nome");
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        JButton btnAlterarEmail = new JButton("Alterar E-mail");
        JButton btnTornarDev = new JButton("Tornar-se Desenvolvedor");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.PLAIN, 14);
        for (JButton b : new JButton[]{btnVerPerfil, btnAlterarNome, btnAlterarSenha, btnAlterarEmail, btnTornarDev, btnVoltar}) {
            b.setFont(fonte);
        }

        // Listeners
        btnVerPerfil.addActionListener(e -> verPerfil());
        btnAlterarNome.addActionListener(e -> alterarNome());
        btnAlterarSenha.addActionListener(e -> alterarSenha());
        btnAlterarEmail.addActionListener(e -> alterarEmail());
        btnTornarDev.addActionListener(e -> tornarDesenvolvedor());
        btnVoltar.addActionListener(e -> dispose());

        // Desabilita o botão de tornar dev se já for Dev ou Operador
        if (conta instanceof Desenvolvedor || conta instanceof Operador) {
            btnTornarDev.setEnabled(false);
            btnTornarDev.setText("Já é Desenvolvedor/Operador");
        }

        panel.add(btnVerPerfil);
        panel.add(btnAlterarNome);
        panel.add(btnAlterarSenha);
        panel.add(btnAlterarEmail);
        panel.add(btnTornarDev);
        panel.add(btnVoltar);

        add(panel);
        setVisible(true);
    }

    // Ver Perfil
    private void verPerfil() {
        String tipo;
        if (usuarioLogado instanceof Desenvolvedor) {
            Desenvolvedor dev = (Desenvolvedor) usuarioLogado;
            tipo = "Desenvolvedor\nEmpresa: " + dev.getEmpresa();
        } else if (usuarioLogado instanceof Operador) {
            tipo = "Operador";
        } else {
            tipo = "Usuário";
        }

        String mensagem = String.format(
                "===== PERFIL =====\n" +
                "ID: %d\n" +
                "Nome: %s\n" +
                "E-mail: %s\n" +
                "Senha: %d\n" +
                "Tipo: %s\n" +
                "Banido: %s",
                usuarioLogado.getId(),
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getSenha(),
                tipo,
                usuarioLogado.getBan() ? "Sim" : "Não"
        );

        // Exibe em um JOptionPane com rolagem (caso a mensagem seja longa)
        JTextArea textArea = new JTextArea(mensagem);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 250));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Meu Perfil",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Alterar Nome
    private void alterarNome() {
        String novoNome = JOptionPane.showInputDialog(this,
                "Digite o novo nome (ou deixe em branco para cancelar):",
                "Alterar Nome", JOptionPane.QUESTION_MESSAGE);
        if (novoNome == null || novoNome.trim().isEmpty()) return;

        String nomeLimpo = novoNome.trim();
        if (nomeLimpo.equals(usuarioLogado.getNome())) {
            JOptionPane.showMessageDialog(this,
                    "O novo nome deve ser diferente do atual.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        usuarioLogado.setNome(nomeLimpo);
        if (salvarAlteracoes()) {
            JOptionPane.showMessageDialog(this,
                    "Nome alterado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao salvar as alterações. Verifique o arquivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Alterar Senha
    private void alterarSenha() {
        String senhaAtualStr = JOptionPane.showInputDialog(this,
                "Digite a senha atual (ou cancelar para abortar):",
                "Alterar Senha", JOptionPane.QUESTION_MESSAGE);
        if (senhaAtualStr == null) return;

        int senhaAtual;
        try {
            senhaAtual = Integer.parseInt(senhaAtualStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "A senha deve ser numérica.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (senhaAtual != usuarioLogado.getSenha()) {
            JOptionPane.showMessageDialog(this,
                    "Senha atual incorreta.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this,
                    "A nova senha deve ser numérica.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (novaSenha == senhaAtual) {
            JOptionPane.showMessageDialog(this,
                    "A nova senha deve ser diferente da atual.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        usuarioLogado.setSenha(novaSenha);
        if (salvarAlteracoes()) {
            JOptionPane.showMessageDialog(this,
                    "Senha alterada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao salvar as alterações. Verifique o arquivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Alterar email    
    private void alterarEmail() {
        String novoEmail = JOptionPane.showInputDialog(this,
                "Digite o novo e-mail:",
                "Alterar E-mail", JOptionPane.QUESTION_MESSAGE);
        if (novoEmail == null || novoEmail.trim().isEmpty()) return;

        String emailLimpo = novoEmail.trim();

        // VALIDAÇÃO: deve conter o caractere '@'
        if (!emailLimpo.contains("@")) {
            JOptionPane.showMessageDialog(this,
                    "E-mail inválido!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifica se o email já está em uso por outra conta
        ArrayList<Conta> contas = Gerenciador.carregaContas();
        if (contas == null) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar contas. Verifique o arquivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Conta c : contas) {
            if (c.getId() != usuarioLogado.getId() &&
                    c.getEmail().equalsIgnoreCase(emailLimpo)) {
                JOptionPane.showMessageDialog(this,
                        "Este e-mail já está em uso por outra conta.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        usuarioLogado.setEmail(emailLimpo);
        if (salvarAlteracoes()) {
            JOptionPane.showMessageDialog(this,
                    "E-mail alterado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao salvar as alterações. Verifique o arquivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tornar-se dev
    private void tornarDesenvolvedor() {
        if (usuarioLogado instanceof Desenvolvedor) {
            JOptionPane.showMessageDialog(this,
                    "Você já é um desenvolvedor.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String empresa = JOptionPane.showInputDialog(this,
                "Digite o nome da sua empresa:",
                "Tornar-se Desenvolvedor", JOptionPane.QUESTION_MESSAGE);
        if (empresa == null || empresa.trim().isEmpty()) return;

        Desenvolvedor dev = new Desenvolvedor(
                usuarioLogado.getId(),
                usuarioLogado.getNome(),
                usuarioLogado.getSenha(),
                usuarioLogado.getEmail(),
                empresa.trim(),
                usuarioLogado.getBan()
        );

        ArrayList<Conta> contas = Gerenciador.carregaContas();
        if (contas == null) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar contas.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean encontrado = false;
        for (int i = 0; i < contas.size(); i++) {
            if (contas.get(i).getId() == dev.getId()) {
                contas.set(i, dev);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this,
                    "Conta original não encontrada na lista.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Gerenciador.salvarContas(contas);
        usuarioLogado = dev;

        JOptionPane.showMessageDialog(this,
                "Conta promovida a Desenvolvedor! Reinicie o menu para ver as novas opções.",
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    // Salvar alterações
    private boolean salvarAlteracoes() {
        try {
            ArrayList<Conta> contas = Gerenciador.carregaContas();
            if (contas == null) {
                return false;
            }

            boolean encontrado = false;
            for (int i = 0; i < contas.size(); i++) {
                if (contas.get(i).getId() == usuarioLogado.getId()) {
                    contas.set(i, usuarioLogado);
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                return false;
            }

            Gerenciador.salvarContas(contas);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}