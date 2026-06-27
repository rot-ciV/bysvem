package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import bysvem.modelo.Compra;
import bysvem.modelo.ItemCompra;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaVisualizarCompras extends JDialog {
    private DefaultTableModel modeloTabela;
    private JTable tabelaCompras;
    private ArrayList<Compra> listaCompras;

    public TelaVisualizarCompras(JFrame parent) {
        super(parent, "Histórico de Compras", true);
        setSize(900, 500);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Todas as Compras Realizadas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID da Compra", "Usuário", "Data", "Valor Total (R$)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
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

        JScrollPane scroll = new JScrollPane(tabelaCompras);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 16));
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        carregarCompras();

        // Duplo clique para ver detalhes
        tabelaCompras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabelaCompras.getSelectedRow();
                    if (row != -1) {
                        int idCompra = (int) modeloTabela.getValueAt(row, 0);
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

        setVisible(true);
    }

    private void carregarCompras() {
        modeloTabela.setRowCount(0);
        listaCompras = new ArrayList<>();

        try {
            EntidadeDAO<Compra> dao = GerenciadorPersistencia.getInstancia().getDAO(Compra.class);
            Compra[] compras = dao.carregarTodos();

            for (Compra compra : compras) {
                listaCompras.add(compra);
                String nomeUsuario = compra.getUsuario() != null ? compra.getUsuario().getNome() : "Desconhecido";
                modeloTabela.addRow(new Object[]{
                    compra.getId(),
                    nomeUsuario,
                    compra.getDataCompra().toString(),
                    String.format("%.2f", compra.getValorTotal())
                });
            }

        }catch(PersistenceException e){
            //Nenhuma compra ainda
        }

        if (modeloTabela.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Nenhuma compra encontrada no sistema.",
                "Informação", JOptionPane.INFORMATION_MESSAGE);
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
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tableItens = new JTable(model);
        tableItens.setFont(new Font("Arial", Font.PLAIN, 14));
        tableItens.setRowHeight(25);
        tableItens.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        for (ItemCompra item : compra.getItens()) {
            model.addRow(new Object[]{
                item.getId(),
                item.getJogo().getNome(),
                String.format("%.2f", item.getPrecoPago())
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