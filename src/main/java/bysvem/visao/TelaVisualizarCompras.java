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
import javax.swing.JDialog;
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

import bysvem.modelo.Compra;
import bysvem.modelo.ItemCompra;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaVisualizarCompras extends JDialog {
    private DefaultTableModel modeloTabela;
    private JTable tabelaCompras;
    private TableRowSorter<DefaultTableModel> sorter;
    private ArrayList<Compra> listaCompras;

    private JTextField campoPesquisa;
    private JTextField campoId;

    public TelaVisualizarCompras(JFrame parent) {
        super(parent, "Histórico de Compras", true);
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        listaCompras = new ArrayList<>();
        carregarCompras();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Todas as Compras Realizadas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // Painel de pesquisa (por nome do usuário e busca por ID)
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblPesquisa = new JLabel("Pesquisar por usuário:");
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
        String[] colunas = {"ID da Compra", "Usuário", "Data", "Valor Total (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;   // ID
                    case 3: return Double.class;    // Valor Total
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaCompras = new JTable(modeloTabela);
        tabelaCompras.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaCompras.setRowHeight(30);
        tabelaCompras.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        tabelaCompras.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabelaCompras.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelaCompras.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaCompras.getColumnModel().getColumn(3).setPreferredWidth(120);

        // Renderizador para valor total (duas casas decimais)
        DefaultTableCellRenderer rendererValor = new DefaultTableCellRenderer() {
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
        rendererValor.setHorizontalAlignment(JLabel.RIGHT);
        tabelaCompras.getColumnModel().getColumn(3).setCellRenderer(rendererValor);

        // Sorter
        sorter = new TableRowSorter<>(modeloTabela);
        sorter.setComparator(0, Comparator.comparingInt(o -> (Integer) o));
        sorter.setComparator(3, Comparator.comparingDouble(o -> (Double) o));
        tabelaCompras.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaCompras);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        // Listeners para filtros
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

        // Duplo clique para ver detalhes
        tabelaCompras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabelaCompras.getSelectedRow();
                    if (row != -1) {
                        int modelRow = tabelaCompras.convertRowIndexToModel(row);
                        int idCompra = (int) modeloTabela.getValueAt(modelRow, 0);
                        for (Compra c : listaCompras) {
                            if (c.getId() == idCompra) {
                                exibirDetalhesCompra(c);
                                break;
                            }
                        }
                    }
                }
            }
        });

        // Preenche a tabela
        atualizarTabela();
        setVisible(true);
    }

    private void aplicarFiltros() {
        String nomeUsuario = campoPesquisa.getText().trim().toLowerCase();
        String idStr = campoId.getText().trim();

        List<RowFilter<DefaultTableModel, Integer>> filtros = new ArrayList<>();

        if (!nomeUsuario.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + nomeUsuario, 1)); // coluna 1 = Usuário
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

    private void carregarCompras() {
        listaCompras.clear();
        try {
            EntidadeDAO<Compra> dao = GerenciadorPersistencia.getInstancia().getDAO(Compra.class);
            Compra[] compras = dao.carregarTodos();
            for (Compra compra : compras) {
                listaCompras.add(compra);
            }
        } catch (PersistenceException e) {
            // Nenhuma compra ainda
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        // Ordena a lista por ID 
        listaCompras.sort(Comparator.comparingInt(Compra::getId));
        for (Compra compra : listaCompras) {
            String nomeUsuario = compra.getUsuario() != null ? compra.getUsuario().getNome() : "Desconhecido";
            modeloTabela.addRow(new Object[]{
                compra.getId(),
                nomeUsuario,
                compra.getDataCompra().toString(),
                compra.getValorTotal()
            });
        }
        if (modeloTabela.getRowCount() == 0) {
        }
    }

    private void exibirDetalhesCompra(Compra compra) {
        JDialog dialog = new JDialog(this, "Detalhes da Compra #" + compra.getId(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Itens da Compra - " + compra.getDataCompra(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Jogo", "Preço (R$)"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 2) return Double.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableItens = new JTable(model);
        tableItens.setFont(new Font("Arial", Font.PLAIN, 14));
        tableItens.setRowHeight(25);
        tableItens.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Renderizador para preço
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
        tableItens.getColumnModel().getColumn(2).setCellRenderer(rendererPreco);

        for (ItemCompra item : compra.getItens()) {
            model.addRow(new Object[]{
                item.getId(),
                item.getJogo().getNome(),
                item.getPrecoPago()
            });
        }

        JScrollPane scroll = new JScrollPane(tableItens);
        panel.add(scroll, BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total: R$ " + String.format("%.2f", compra.getValorTotal()));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(totalLabel, BorderLayout.SOUTH);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFechar.addActionListener(e -> dialog.dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnFechar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}