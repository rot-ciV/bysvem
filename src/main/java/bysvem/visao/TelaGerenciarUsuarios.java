package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaGerenciarUsuarios extends JDialog {

    private ArrayList<Conta> contas;
    private DefaultTableModel modeloTabela;
    private JTable tabelaContas;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField campoPesquisa;
    private JTextField campoId;
    private JCheckBox chkApenasBanidos;

    public TelaGerenciarUsuarios(JFrame parent) {
        super(parent, "Gerenciar Usuários", true);
        setSize(1000, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        contas = new ArrayList<>();
        try {
            Conta[] vetorContas = GerenciadorPersistencia.getInstancia().getDAO(Conta.class).carregarTodos();
            for (Conta contaAtual : vetorContas) {
                contas.add(contaAtual);
            }
        } catch (PersistenceException e) {
            // Nenhuma conta ainda
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciar Usuários e Desenvolvedores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        // Painel de pesquisa e filtros
        JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JLabel lblPesquisa = new JLabel("Pesquisar por nome:");
        lblPesquisa.setFont(new Font("Arial", Font.BOLD, 14));
        campoPesquisa = new JTextField(20);
        campoPesquisa.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel lblBuscarId = new JLabel("Buscar ID:");
        lblBuscarId.setFont(new Font("Arial", Font.BOLD, 14));
        campoId = new JTextField(8);
        campoId.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton btnBuscarId = new JButton("Buscar");
        btnBuscarId.setFont(new Font("Arial", Font.BOLD, 12));

        chkApenasBanidos = new JCheckBox("Mostrar apenas banidos");
        chkApenasBanidos.setFont(new Font("Arial", Font.BOLD, 14));

        painelPesquisa.add(lblPesquisa);
        painelPesquisa.add(campoPesquisa);
        painelPesquisa.add(btnLimpar);
        painelPesquisa.add(new JLabel("   "));
        painelPesquisa.add(lblBuscarId);
        painelPesquisa.add(campoId);
        painelPesquisa.add(btnBuscarId);
        painelPesquisa.add(new JLabel("   "));
        painelPesquisa.add(chkApenasBanidos);

        panel.add(painelPesquisa, BorderLayout.NORTH);

        // Tabela com tipos bem definidos
        String[] colunas = {"ID", "Nome", "Tipo", "Banido?"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;   // ID
                    case 3: return Boolean.class;   // Banido
                    default: return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaContas = new JTable(modeloTabela);
        tabelaContas.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaContas.setRowHeight(25);
        tabelaContas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Ajuste de largura
        tabelaContas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaContas.getColumnModel().getColumn(1).setPreferredWidth(250);
        tabelaContas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaContas.getColumnModel().getColumn(3).setPreferredWidth(80);

        // Sorter para ordenação por clique no cabeçalho
        sorter = new TableRowSorter<>(modeloTabela);
        // Garantir ordenação numérica para ID e Boolean para Banido
        sorter.setComparator(0, (o1, o2) -> ((Integer) o1).compareTo((Integer) o2));
        sorter.setComparator(3, (o1, o2) -> ((Boolean) o1).compareTo((Boolean) o2));
        tabelaContas.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaContas);
        panel.add(scroll, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnBanir = new JButton("Banir/Desbanir");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.BOLD, 16);
        btnBanir.setFont(fonte);
        btnVoltar.setFont(fonte);

        painelBotoes.add(btnBanir);
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        // Listeners
        campoPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        campoId.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltros(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltros(); }
        });

        btnLimpar.addActionListener(e -> {
            campoPesquisa.setText("");
            campoId.setText("");
            chkApenasBanidos.setSelected(false);
            sorter.setRowFilter(null);
        });

        btnBuscarId.addActionListener(e -> aplicarFiltros());
        chkApenasBanidos.addActionListener(e -> aplicarFiltros());

        btnBanir.addActionListener(e -> banirDesbanir());
        btnVoltar.addActionListener(e -> dispose());

        // Preencher a tabela inicialmente
        atualizarTabela();
        setVisible(true);
    }

    private void aplicarFiltros() {
        String nome = campoPesquisa.getText().trim().toLowerCase();
        String idStr = campoId.getText().trim();
        boolean apenasBanidos = chkApenasBanidos.isSelected();

        List<RowFilter<DefaultTableModel, Integer>> filtros = new ArrayList<>();

        // Filtro por nome (coluna 1)
        if (!nome.isEmpty()) {
            filtros.add(RowFilter.regexFilter("(?i)" + nome, 1));
        }

        // Filtro por ID (coluna 0)
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

        // Filtro por banidos (coluna 3 – Boolean)
        if (apenasBanidos) {
            filtros.add(RowFilter.regexFilter("true", 3));
        }

        if (filtros.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filtros));
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        contas.sort((c1, c2) -> Integer.compare(c1.getId(), c2.getId()));
        for (Conta c : contas) {
            // Filtra apenas Usuário ou Desenvolvedor
            if (!(c instanceof Usuario) && !(c instanceof Desenvolvedor)) {
                continue;
            }
            String tipo = (c instanceof Usuario) ? "Usuário" : "Desenvolvedor";
            modeloTabela.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                tipo,
                c.getBan()
            });
        }
    }

    private void banirDesbanir() {
        int selectedRow = tabelaContas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Converte índice da view para o modelo (por causa do sorter)
        int modelRow = tabelaContas.convertRowIndexToModel(selectedRow);
        int id = (int) modeloTabela.getValueAt(modelRow, 0);

        Conta selecionada = null;
        for (Conta c : contas) {
            if (c.getId() == id) {
                selecionada = c;
                break;
            }
        }
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this, "Erro ao localizar a conta.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String acao = selecionada.getBan() ? "desbanir" : "banir";
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja " + acao + " o usuário " + selecionada.getNome() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            selecionada.setBan(!selecionada.getBan());
            try {
                EntidadeDAO<Conta> dao = GerenciadorPersistencia.getInstancia().getDAO(Conta.class);
                dao.atualizar(selecionada);
                dao.persistir("dados/contas.dat");

                // Atualiza a tabela e os filtros
                atualizarTabela();
                aplicarFiltros(); // reaplica os filtros atuais

                JOptionPane.showMessageDialog(this,
                        "Usuário " + (selecionada.getBan() ? "banido" : "desbanido") + " com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (PersistenceException e) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                selecionada.setBan(!selecionada.getBan()); // reverte em caso de falha
            }
        }
    }
}