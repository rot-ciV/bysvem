// bysvem/visao/TelaGerenciarJogos.java

package bysvem.visao;

import bysvem.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaGerenciarJogos extends JDialog {
    private Desenvolvedor desenvolvedor;
    private ArrayList<Jogo> jogosDoDev;
    private DefaultListModel<String> modelo;
    private JList<String> listaJogos;

    public TelaGerenciarJogos(JFrame parent, Desenvolvedor dev) {
        super(parent, "Gerenciar Jogos", true);
        this.desenvolvedor = dev;
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Carrega jogos da desenvolvedora
        ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
        jogosDoDev = new ArrayList<>();
        for (Jogo j : todosJogos) {
            if (j.getDesenvolvedora().equalsIgnoreCase(dev.getEmpresa())) {
                jogosDoDev.add(j);
            }
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Jogos da " + dev.getEmpresa(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        atualizarLista();

        listaJogos = new JList<>(modelo);
        listaJogos.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(listaJogos);
        panel.add(scroll, BorderLayout.CENTER);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar Jogo");
        JButton btnEditar = new JButton("Editar Jogo");
        JButton btnRemover = new JButton("Remover Jogo");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.BOLD, 16);
        for (JButton b : new JButton[]{btnAdicionar, btnEditar, btnRemover, btnVoltar}) {
            b.setFont(fonte);
        }

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        // Listeners
        btnAdicionar.addActionListener(e -> adicionarJogo());
        btnEditar.addActionListener(e -> editarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void atualizarLista() {
        modelo.clear();
        for (Jogo j : jogosDoDev) {
            modelo.addElement(j.getNome() + " (R$ " + String.format("%.2f", j.getPreco()) + ")");
        }
    }

    private void adicionarJogo() {
        // Cria um painel com campos para o novo jogo
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField campoNome = new JTextField();
        JTextField campoGenero = new JTextField();
        JTextField campoPreco = new JTextField();
        JTextArea campoDesc = new JTextArea(3, 20);
        campoDesc.setLineWrap(true);
        campoDesc.setWrapStyleWord(true);

        panel.add(new JLabel("Nome:"));
        panel.add(campoNome);
        panel.add(new JLabel("Gênero:"));
        panel.add(campoGenero);
        panel.add(new JLabel("Preço:"));
        panel.add(campoPreco);
        panel.add(new JLabel("Descrição:"));
        panel.add(new JScrollPane(campoDesc));

        int result = JOptionPane.showConfirmDialog(this, panel, "Novo Jogo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = campoNome.getText().trim();
                String genero = campoGenero.getText().trim();
                double preco = Double.parseDouble(campoPreco.getText().trim());
                String desc = campoDesc.getText().trim();

                if (nome.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e gênero são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Gera um ID para o jogo
                ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
                int novoId = 1;
                for (Jogo j : todosJogos) {
                    if (j.getId() >= novoId) {
                        novoId = j.getId() + 1;
                    }
                }
                int id = novoId;

                Jogo novoJogo = new Jogo(id, nome, genero, desenvolvedor.getEmpresa(), preco, desc);
                if (novoJogo.salvar(todosJogos)) {
                    jogosDoDev.add(novoJogo);
                    atualizarLista();
                    JOptionPane.showMessageDialog(this, "Jogo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarJogo() {
        int idx = listaJogos.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Jogo jogo = jogosDoDev.get(idx);

        // Painel com campos preenchidos
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField campoNome = new JTextField(jogo.getNome());
        JTextField campoGenero = new JTextField(jogo.getGenero());
        JTextField campoPreco = new JTextField(String.valueOf(jogo.getPreco()));
        JTextArea campoDesc = new JTextArea(jogo.getDesc(), 3, 20);
        campoDesc.setLineWrap(true);
        campoDesc.setWrapStyleWord(true);

        panel.add(new JLabel("Nome:"));
        panel.add(campoNome);
        panel.add(new JLabel("Gênero:"));
        panel.add(campoGenero);
        panel.add(new JLabel("Preço:"));
        panel.add(campoPreco);
        panel.add(new JLabel("Descrição:"));
        panel.add(new JScrollPane(campoDesc));

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Jogo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = campoNome.getText().trim();
                String genero = campoGenero.getText().trim();
                double preco = Double.parseDouble(campoPreco.getText().trim());
                String desc = campoDesc.getText().trim();

                if (nome.isEmpty() || genero.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nome e gênero são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                jogo.setNome(nome);
                jogo.setGenero(genero);
                jogo.setPreco(preco);
                jogo.setDesc(desc);

                ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
                if (jogo.atualizar(todosJogos)) {
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

        Jogo jogo = jogosDoDev.get(idx);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o jogo '" + jogo.getNome() + "'?\n" +
                "Todos os usuários que compraram este jogo serão reembolsados.",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Gerenciador.removerJogoComReembolso(jogo);
            // Recarrega os jogos do desenvolvedor
            ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
            jogosDoDev.clear();
            for (Jogo j : todosJogos) {
                if (j.getDesenvolvedora().equalsIgnoreCase(desenvolvedor.getEmpresa())) {
                    jogosDoDev.add(j);
                }
            }
            atualizarLista();
            JOptionPane.showMessageDialog(this, "Jogo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}