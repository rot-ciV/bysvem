package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import bysvem.modelo.Jogo;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class Jogos_Disponiveis extends JFrame {

    private ArrayList<Jogo> jogos;
    private DefaultTableModel modeloTabela;
    private JTable tabelaJogos;
    private TableRowSorter<DefaultTableModel> sorter;
    private Usuario usuarioLogado;

    private JTextField campoPesquisa;
    private JTextField campoId;

    public Jogos_Disponiveis(Usuario usuario) {
        this.usuarioLogado = usuario;
        setTitle("Jogos Disponíveis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        jogos = carregarTodosJogos();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Catálogo de Jogos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        // Painel de pesquisa (por nome e busca por ID)
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
                    case 0: return Integer.class;   // ID
                    case 4: return Double.class;    // Preço (armazenamos como Double)
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

        // Ajuste de largura das colunas
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
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnDetalhes = new JButton("Ver detalhes");
        JButton btnVoltar = new JButton("Voltar");

        btnDetalhes.setFont(new Font("Arial", Font.BOLD, 20));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 20));

        panelBotoes.add(btnDetalhes);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);

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

        btnDetalhes.addActionListener(e -> {
            int row = tabelaJogos.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um jogo na tabela.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = tabelaJogos.convertRowIndexToModel(row);
            int id = (int) modeloTabela.getValueAt(modelRow, 0);
            Jogo jogoSelecionado = null;
            for (Jogo j : jogos) {
                if (j.getId() == id) {
                    jogoSelecionado = j;
                    break;
                }
            }
            if (jogoSelecionado != null) {
                Detalhes_Usuario.exibirDetalhes(this, jogoSelecionado, true, usuarioLogado);
            }
        });

        tabelaJogos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabelaJogos.getSelectedRow();
                    if (row != -1) {
                        int modelRow = tabelaJogos.convertRowIndexToModel(row);
                        int id = (int) modeloTabela.getValueAt(modelRow, 0);
                        Jogo jogoSelecionado = null;
                        for (Jogo j : jogos) {
                            if (j.getId() == id) {
                                jogoSelecionado = j;
                                break;
                            }
                        }
                        if (jogoSelecionado != null) {
                            Detalhes_Usuario.exibirDetalhes(Jogos_Disponiveis.this, jogoSelecionado, true, usuarioLogado);
                        }
                    }
                }
            }
        });

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

    private ArrayList<Jogo> carregarTodosJogos() {
        ArrayList<Jogo> lista = new ArrayList<>();
        try {
            EntidadeDAO<Jogo> dao = GerenciadorPersistencia.getInstancia().getDAO(Jogo.class);
            Jogo[] array = dao.carregarTodos();
            for (Jogo j : array) lista.add(j);
        } catch (PersistenceException e) {
            // Nenhum jogo ainda
        }
        return lista;
    }
}