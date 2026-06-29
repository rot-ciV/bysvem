package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Jogo;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaGerenciarJogos extends JDialog {
    private Desenvolvedor desenvolvedor;
    private ArrayList<Jogo> jogosDoDev;
    private DefaultTableModel modeloTabela;
    private JTable tabelaJogos;
    private TableRowSorter<DefaultTableModel> sorter;

    public TelaGerenciarJogos(JFrame parent, Desenvolvedor dev) {
        super(parent, "Gerenciar Jogos", true);
        this.desenvolvedor = dev;
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        ArrayList<Jogo> todosJogos = carregarTodosJogos();
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

        // Tabela
        String[] colunas = {"ID", "Nome", "Gênero", "Preço (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaJogos = new JTable(modeloTabela);
        tabelaJogos.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaJogos.setRowHeight(25);
        tabelaJogos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Ajusta largura das colunas
        tabelaJogos.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabelaJogos.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nome
        tabelaJogos.getColumnModel().getColumn(2).setPreferredWidth(150);  // Gênero
        tabelaJogos.getColumnModel().getColumn(3).setPreferredWidth(100);  // Preço

        sorter = new TableRowSorter<>(modeloTabela);
        tabelaJogos.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaJogos);
        panel.add(scroll, BorderLayout.CENTER);

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

        btnAdicionar.addActionListener(e -> adicionarJogo());
        btnEditar.addActionListener(e -> editarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnVoltar.addActionListener(e -> dispose());

        atualizarTabela();
        setVisible(true);
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        jogosDoDev.sort(Comparator.comparingInt(Jogo::getId));
        for (Jogo j : jogosDoDev) {
            modeloTabela.addRow(new Object[]{
                j.getId(),
                j.getNome(),
                j.getGenero(),
                String.format("%.2f", j.getPreco())
            });
        }
    }

    private void adicionarJogo() {
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

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String nome = campoNome.getText().trim();
            String genero = campoGenero.getText().trim();
            double preco = Double.parseDouble(campoPreco.getText().trim());
            String desc = campoDesc.getText().trim();

            Jogo novoJogo = desenvolvedor.adicionarJogo(nome, genero, preco, desc);

            jogosDoDev.add(novoJogo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Jogo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarJogo() {
        int row = tabelaJogos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = tabelaJogos.convertRowIndexToModel(row);
        int id = (int) modeloTabela.getValueAt(modelRow, 0);
        Jogo jogo = null;
        for (Jogo j : jogosDoDev) {
            if (j.getId() == id) {
                jogo = j;
                break;
            }
        }
        if (jogo == null) {
            JOptionPane.showMessageDialog(this, "Erro ao localizar o jogo.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Painel de edição 
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

        if (result != JOptionPane.OK_OPTION) return;

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

            EntidadeDAO<Jogo> dao = GerenciadorPersistencia.getInstancia().getDAO(Jogo.class);
            dao.atualizar(jogo);
            dao.persistir("dados/jogos.dat");

            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Jogo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerJogo() {
        int row = tabelaJogos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = tabelaJogos.convertRowIndexToModel(row);
        int id = (int) modeloTabela.getValueAt(modelRow, 0);
        Jogo jogo = null;
        for (Jogo j : jogosDoDev) {
            if (j.getId() == id) {
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
                "Todos os usuários que compraram este jogo serão reembolsados.",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            desenvolvedor.removerJogo(jogo);
            jogosDoDev.remove(jogo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Jogo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Jogo> carregarTodosJogos() {
        ArrayList<Jogo> lista = new ArrayList<>();
        try {
            Jogo[] array = GerenciadorPersistencia.getInstancia().getDAO(Jogo.class).carregarTodos();
            for (Jogo j : array) lista.add(j);
        } catch (PersistenceException e) {
            // vazio
        }
        return lista;
    }

}