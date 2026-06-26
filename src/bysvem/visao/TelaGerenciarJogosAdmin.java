// bysvem/visao/TelaGerenciarJogosAdmin.java

package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import bysvem.modelo.Gerenciador;
import bysvem.modelo.Jogo;

public class TelaGerenciarJogosAdmin extends JDialog {

    private ArrayList<Jogo> jogos;
    private DefaultTableModel modeloTable;
    private JTable tabelaJogos;

    private JTextField campoPesquisa;
    private JComboBox<String> comboOrdenacao;

    public TelaGerenciarJogosAdmin(JFrame parent) {
        super(parent, "Gerenciar Jogos (Admin)", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        jogos = Gerenciador.carregaJogos();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciar Jogos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel painelControle = new JPanel(new BorderLayout(10, 10));

        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblPesquisa = new JLabel("Pesquisar por nome:");
        lblPesquisa.setFont(new Font("Arial", Font.BOLD, 14));
        campoPesquisa = new JTextField(20);
        campoPesquisa.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 12));

        painelPesquisa.add(lblPesquisa);
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(btnLimpar);

        JPanel painelOrdenacao = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 14));
        String[] opcoes = {"Nome (A-Z)", "Nome (Z-A)", "Preço (crescente)", "Preço (decrescente)"};
        comboOrdenacao = new JComboBox<>(opcoes);
        comboOrdenacao.setFont(new Font("Arial", Font.PLAIN, 14));
        comboOrdenacao.setPreferredSize(new Dimension(200, 25));

        painelOrdenacao.add(lblOrdenar);
        painelOrdenacao.add(comboOrdenacao);

        painelControle.add(painelPesquisa, BorderLayout.NORTH);
        painelControle.add(painelOrdenacao, BorderLayout.SOUTH);

        panel.add(painelControle, BorderLayout.NORTH);

        modeloTable = new DefaultTableModel(new Object[]{"Nome", "Preço (R$)", "Desenvolvedora"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaJogos = new JTable(modeloTable);
        tabelaJogos.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaJogos.setRowHeight(25);
        tabelaJogos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        tabelaJogos.getColumnModel().getColumn(0).setPreferredWidth(300); // Nome
        tabelaJogos.getColumnModel().getColumn(1).setPreferredWidth(100); // Preço
        tabelaJogos.getColumnModel().getColumn(2).setPreferredWidth(200); // Desenvolvedora

        JScrollPane scroll = new JScrollPane(tabelaJogos);
        panel.add(scroll, BorderLayout.CENTER);

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

        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { aplicarFiltroEOrdenacao(); }
            @Override
            public void removeUpdate(DocumentEvent e) { aplicarFiltroEOrdenacao(); }
            @Override
            public void changedUpdate(DocumentEvent e) { aplicarFiltroEOrdenacao(); }
        });

        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            aplicarFiltroEOrdenacao();
        });

        comboOrdenacao.addActionListener(e -> aplicarFiltroEOrdenacao());

        btnEditar.addActionListener(e -> editarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnVoltar.addActionListener(e -> dispose());

        aplicarFiltroEOrdenacao();

        setVisible(true);
    }


    private void aplicarFiltroEOrdenacao() {
        String texto = campoPesquisa.getText().trim().toLowerCase();
        ArrayList<Jogo> filtrados = new ArrayList<>();
        if (texto.isEmpty()) {
            filtrados.addAll(jogos);
        } else {
            for (Jogo j : jogos) {
                if (j.getNome().toLowerCase().contains(texto)) {
                    filtrados.add(j);
                }
            }
        }

        int opcao = comboOrdenacao.getSelectedIndex();
        Comparator<Jogo> comparator;
        switch (opcao) {
            case 0: // Nome A-Z
                comparator = Comparator.comparing(Jogo::getNome, String.CASE_INSENSITIVE_ORDER);
                break;
            case 1: // Nome Z-A
                comparator = Comparator.comparing(Jogo::getNome, String.CASE_INSENSITIVE_ORDER).reversed();
                break;
            case 2: // Preço crescente
                comparator = Comparator.comparingDouble(Jogo::getPreco);
                break;
            case 3: // Preço decrescente
                comparator = Comparator.comparingDouble(Jogo::getPreco).reversed();
                break;
            default:
                comparator = Comparator.comparing(Jogo::getNome, String.CASE_INSENSITIVE_ORDER);
        }
        Collections.sort(filtrados, comparator);

        modeloTable.setRowCount(0);
        for (Jogo j : filtrados) {
            modeloTable.addRow(new Object[]{
                j.getNome(),
                String.format("%.2f", j.getPreco()),
                j.getDesenvolvedora()
            });
        }
    }


    private void editarJogo() {
        int selectedRow = tabelaJogos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeSelecionado = (String) modeloTable.getValueAt(selectedRow, 0);
        Jogo jogo = null;
        for (Jogo j : jogos) {
            if (j.getNome().equals(nomeSelecionado)) {
                jogo = j;
                break;
            }
        }
        if (jogo == null) {
            JOptionPane.showMessageDialog(this, "Erro ao localizar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                    jogos = Gerenciador.carregaJogos(); // recarrega
                    aplicarFiltroEOrdenacao();
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
        int selectedRow = tabelaJogos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeSelecionado = (String) modeloTable.getValueAt(selectedRow, 0);
        Jogo jogo = null;
        for (Jogo j : jogos) {
            if (j.getNome().equals(nomeSelecionado)) {
                jogo = j;
                break;
            }
        }
        if (jogo == null) {
            JOptionPane.showMessageDialog(this, "Erro ao localizar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o jogo '" + jogo.getNome() + "'?\n" +
                "Todos os compradores serão reembolsados automaticamente.",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Gerenciador.removerJogoComReembolso(jogo);
            if (jogo.apagar(jogo.getId(), jogos)) {
                jogos = Gerenciador.carregaJogos();
                aplicarFiltroEOrdenacao();
                JOptionPane.showMessageDialog(this, "Jogo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao remover jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}