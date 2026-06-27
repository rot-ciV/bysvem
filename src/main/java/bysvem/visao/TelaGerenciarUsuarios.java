// bysvem/visao/TelaGerenciarUsuarios.java

package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaGerenciarUsuarios extends JDialog {

    private ArrayList<Conta> contas;
    private DefaultTableModel modeloTable;
    private JTable tabelaContas;

    private JTextField campoPesquisa;
    private JComboBox<String> comboOrdenacao;
    private JCheckBox chkApenasBanidos;

    public TelaGerenciarUsuarios(JFrame parent) {
        super(parent, "Gerenciar Usuários", true);
        setSize(900, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        contas = new ArrayList<>();
        try {
            Conta[] vetorContas = GerenciadorPersistencia.getInstancia().getDAO(Conta.class).carregarTodos();

            for (Conta contaAtual : vetorContas){
                contas.add(contaAtual);
            }
        } catch (PersistenceException e) {
            //Nao tem nenhuma conta 
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Gerenciar Usuários e Desenvolvedores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel painelControle = new JPanel(new BorderLayout(10, 10));

        // Subpainel: pesquisa
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

        // Subpainel: ordenação e filtro de banidos
        JPanel painelOrdenacaoFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel lblOrdenar = new JLabel("Ordenar por:");
        lblOrdenar.setFont(new Font("Arial", Font.BOLD, 14));
        String[] opcoes = {"Nome (A-Z)", "Nome (Z-A)", "Tipo (Usuário primeiro)", "Tipo (Desenvolvedor primeiro)"};
        comboOrdenacao = new JComboBox<>(opcoes);
        comboOrdenacao.setFont(new Font("Arial", Font.PLAIN, 14));
        comboOrdenacao.setPreferredSize(new Dimension(200, 25));

        chkApenasBanidos = new JCheckBox("Mostrar apenas banidos");
        chkApenasBanidos.setFont(new Font("Arial", Font.BOLD, 14));

        painelOrdenacaoFiltro.add(lblOrdenar);
        painelOrdenacaoFiltro.add(comboOrdenacao);
        painelOrdenacaoFiltro.add(chkApenasBanidos);

        painelControle.add(painelPesquisa, BorderLayout.NORTH);
        painelControle.add(painelOrdenacaoFiltro, BorderLayout.SOUTH);

        panel.add(painelControle, BorderLayout.NORTH);

        modeloTable = new DefaultTableModel(new Object[]{"ID", "Nome", "Tipo", "Banido?"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaContas = new JTable(modeloTable);
        tabelaContas.setFont(new Font("Arial", Font.PLAIN, 16));
        tabelaContas.setRowHeight(25);
        tabelaContas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        tabelaContas.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabelaContas.getColumnModel().getColumn(1).setPreferredWidth(250);  // Nome
        tabelaContas.getColumnModel().getColumn(2).setPreferredWidth(150);  // Tipo
        tabelaContas.getColumnModel().getColumn(3).setPreferredWidth(100);  // Banido?

        JScrollPane scroll = new JScrollPane(tabelaContas);
        panel.add(scroll, BorderLayout.CENTER);

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
        chkApenasBanidos.addActionListener(e -> aplicarFiltroEOrdenacao());

        btnBanir.addActionListener(e -> banirDesbanir());
        btnVoltar.addActionListener(e -> dispose());

        aplicarFiltroEOrdenacao();

        setVisible(true);
    }


    private void aplicarFiltroEOrdenacao() {
        String texto = campoPesquisa.getText().trim().toLowerCase();
        boolean apenasBanidos = chkApenasBanidos.isSelected();

        ArrayList<Conta> filtradas = new ArrayList<>();
        for (Conta c : contas) {
            if (!(c instanceof Usuario) && !(c instanceof Desenvolvedor)) {
                continue;
            }
            if (!c.getNome().toLowerCase().contains(texto)) {
                continue;
            }
            if (apenasBanidos && !c.getBan()) {
                continue;
            }
            filtradas.add(c);
        }

        int opcao = comboOrdenacao.getSelectedIndex();
        Comparator<Conta> comparator;
        switch (opcao) {
            case 0: // Nome A-Z
                comparator = Comparator.comparing(Conta::getNome, String.CASE_INSENSITIVE_ORDER);
                break;
            case 1: // Nome Z-A
                comparator = Comparator.comparing(Conta::getNome, String.CASE_INSENSITIVE_ORDER).reversed();
                break;
            case 2: // Tipo Usuário primeiro
                comparator = Comparator.comparing(c -> (c instanceof Usuario) ? 0 : 1);
                break;
            case 3: // Tipo Desenvolvedor primeiro
                comparator = Comparator.comparing(c -> (c instanceof Desenvolvedor) ? 0 : 1);
                break;
            default:
                comparator = Comparator.comparing(Conta::getNome, String.CASE_INSENSITIVE_ORDER);
        }
        // Em caso de empate, ordenar por nome
        comparator = comparator.thenComparing(Conta::getNome, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(filtradas, comparator);

        modeloTable.setRowCount(0);
        for (Conta c : filtradas) {
            String tipo = (c instanceof Usuario) ? "Usuário" : "Desenvolvedor";
            String banido = c.getBan() ? "Sim" : "Não";
            modeloTable.addRow(new Object[]{
                c.getId(),
                c.getNome(),
                tipo,
                banido
            });
        }
    }


    private void banirDesbanir() {
        int selectedRow = tabelaContas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTable.getValueAt(selectedRow, 0);
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

        if (confirm == JOptionPane.YES_OPTION){
            selecionada.setBan(!selecionada.getBan());
            try{
                EntidadeDAO<Conta> dao = GerenciadorPersistencia.getInstancia().getDAO(Conta.class);
                dao.atualizar(selecionada);
                dao.persistir("dados/contas.dat");

            }catch(PersistenceException e){
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                selecionada.setBan(!selecionada.getBan()); // reverte em caso de falha
                return;
            }

            aplicarFiltroEOrdenacao();
            JOptionPane.showMessageDialog(this,
            "Usuário " + (selecionada.getBan() ? "banido" : "desbanido") + " com sucesso!",
            "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}