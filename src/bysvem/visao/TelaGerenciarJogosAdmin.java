// bysvem/visao/TelaGerenciarJogosAdmin.java (sem botão Adicionar)

package bysvem.visao;

import bysvem.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaGerenciarJogosAdmin extends JDialog {
    private ArrayList<Jogo> jogos;
    private DefaultListModel<String> modelo;
    private JList<String> listaJogos;

    public TelaGerenciarJogosAdmin(JFrame parent) {
        super(parent, "Gerenciar Jogos (Admin)", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        jogos = Gerenciador.carregaJogos();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciar Jogos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        atualizarLista();

        listaJogos = new JList<>(modelo);
        listaJogos.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(listaJogos);
        panel.add(scroll, BorderLayout.CENTER);

        // Painel de botões SEM ADICIONAR
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnEditar = new JButton("Editar Jogo");
        JButton btnRemover = new JButton("Remover Jogo");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.BOLD, 16);
        for (JButton b : new JButton[]{btnEditar, btnRemover, btnVoltar}) {
            b.setFont(fonte);
        }

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        btnEditar.addActionListener(e -> editarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void atualizarLista() {
        modelo.clear();
        jogos = Gerenciador.carregaJogos(); // recarrega para manter sincronizado
        for (Jogo j : jogos) {
            modelo.addElement(j.getNome() + " (R$ " + String.format("%.2f", j.getPreco()) + ") - " + j.getDesenvolvedora());
        }
    }

    private void editarJogo() {
        int idx = listaJogos.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Jogo jogo = jogos.get(idx);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        JTextField campoNome = new JTextField(jogo.getNome());
        JTextField campoGenero = new JTextField(jogo.getGenero());
        JTextField campoPreco = new JTextField(String.valueOf(jogo.getPreco()));
        JTextField campoDesenvolvedora = new JTextField(jogo.getDesenvolvedora());
        JTextArea campoDesc = new JTextArea(jogo.getDesc(), 3, 20);
        campoDesc.setLineWrap(true);
        campoDesc.setWrapStyleWord(true);

        panel.add(new JLabel("Nome:"));
        panel.add(campoNome);
        panel.add(new JLabel("Gênero:"));
        panel.add(campoGenero);
        panel.add(new JLabel("Preço:"));
        panel.add(campoPreco);
        panel.add(new JLabel("Desenvolvedora:"));
        panel.add(campoDesenvolvedora);
        panel.add(new JLabel("Descrição:"));
        panel.add(new JScrollPane(campoDesc));

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Jogo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = campoNome.getText().trim();
                String genero = campoGenero.getText().trim();
                double preco = Double.parseDouble(campoPreco.getText().trim());
                String desenvolvedora = campoDesenvolvedora.getText().trim();
                String desc = campoDesc.getText().trim();

                if (nome.isEmpty() || genero.isEmpty() || desenvolvedora.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                jogo.setNome(nome);
                jogo.setGenero(genero);
                jogo.setPreco(preco);
                jogo.setDesenvolvedora(desenvolvedora);
                jogo.setDesc(desc);

                if (jogo.atualizar(jogos)) {
                    atualizarLista();
                    JOptionPane.showMessageDialog(this, "Jogo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerJogo() {
        int idx = listaJogos.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jogo jogo = jogos.get(idx);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o jogo '" + jogo.getNome() + "'?\n" +
                "Todos os compradores serão reembolsados automaticamente.",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Reembolsa os compradores
            Gerenciador.removerJogoComReembolso(jogo);

            // Apaga o jogo
            if (jogo.apagar(jogo.getId(), jogos)) {
                atualizarLista();
                JOptionPane.showMessageDialog(this, "Jogo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}