package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import bysvem.modelo.Jogo;
import bysvem.modelo.Operador;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaGerenciarJogosAdmin extends JDialog {

    private ArrayList<Jogo> jogos;
    private DefaultTableModel modeloTabela;
    private JTable tabelaJogos;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField campoPesquisa;
    private JTextField campoId;

    public TelaGerenciarJogosAdmin(JFrame parent) {
        super(parent, "Gerenciar Jogos (Admin)", true);
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        jogos = carregarTodosJogos();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciar Jogos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // Painel de pesquisa (nome e busca por ID)
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblPesquisa = new JLabel("Pesquisar por nome:");
        lblPesquisa.setFont(new Font("Arial", Font.BOLD, 14));
        campoPesquisa = new JTextField(20);
        campoPesquisa.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblBuscarId = new JLabel("Buscar ID:");
        lblBuscarId.setFont(new Font("Arial", Font.BOLD, 14));
        campoId = new JTextField(10);
        campoId.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton btnBuscarId = new JButton("Buscar");
        btnBuscarId.setFont(new Font("Arial", Font.BOLD, 12));

        painelPesquisa.add(lblPesquisa);
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(btnLimpar);
        painelPesquisa.add(new JLabel("   "));
        painelPesquisa.add(lblBuscarId);
        painelPesquisa.add(campoId);
        painelPesquisa.add(btnBuscarId);

        panel.add(painelPesquisa, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Nome", "Gênero", "Desenvolvedora", "Preço (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 4: return Double.class;
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaJogos = new JTable(modeloTabela);
        tabelaJogos.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaJogos.setRowHeight(25);
        tabelaJogos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        tabelaJogos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaJogos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaJogos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaJogos.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabelaJogos.getColumnModel().getColumn(4).setPreferredWidth(100);

        DefaultTableCellRenderer rendererPreco = new DefaultTableCellRenderer() {
            private final DecimalFormat df = new DecimalFormat("0.00");
            @Override
            protected void setValue(Object value) {
                if (value instanceof Double) {
                    setText(df.format(value));
                } else {
                    super.setValue(value);
                }
            }
        };
        rendererPreco.setHorizontalAlignment(JLabel.RIGHT);
        tabelaJogos.getColumnModel().getColumn(4).setCellRenderer(rendererPreco);

        sorter = new TableRowSorter<>(modeloTabela);
        sorter.setComparator(0, Comparator.comparingInt(o -> (Integer) o));
        sorter.setComparator(4, Comparator.comparingDouble(o -> (Double) o));
        tabelaJogos.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaJogos);
        panel.add(scroll, BorderLayout.CENTER);

        // Botões
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
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            campoId.setText("");
            sorter.setRowFilter(null);
        });

        btnBuscarId.addActionListener(e -> aplicarFiltros());

        btnEditar.addActionListener(e -> editarJogo());
        btnRemover.addActionListener(e -> removerJogo());
        btnVoltar.addActionListener(e -> dispose());

        atualizarTabela();
        setVisible(true);
    }

    private void aplicarFiltros() {
        String nome = campoPesquisa.getText().trim().toLowerCase();
        String idStr = campoId.getText().trim();

        List<RowFilter<DefaultTableModel, Integer>> filtros = new ArrayList<>();

        if (!nome.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + nome, 1));
        }

        if (!idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                filtros.add(RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, id, 0));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido. Digite um número.");
                campoId.setText("");
                return;
            }
        }

        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        jogos.sort(Comparator.comparingInt(Jogo::getId));
        for (Jogo j : jogos) {
            modeloTabela.addRow(new Object[]{
                j.getId(),
                j.getNome(),
                j.getGenero(),
                j.getDesenvolvedora(),
                j.getPreco()
            });
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
        for (Jogo j : jogos) {
            if (j.getId() == id) {
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

                EntidadeDAO<Jogo> dao = GerenciadorPersistencia.getInstancia().getDAO(Jogo.class);
                dao.atualizar(jogo);
                dao.persistir("dados/jogos.dat");
                jogos = carregarTodosJogos();
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Jogo atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (PersistenceException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
        for (Jogo j : jogos) {
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
                "Todos os compradores serão reembolsados automaticamente.",
                "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            Operador.removerJogo(jogo); 

            jogos = carregarTodosJogos();
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