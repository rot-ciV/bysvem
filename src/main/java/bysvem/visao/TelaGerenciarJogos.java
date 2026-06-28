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

import bysvem.modelo.Compra;
import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Entidade;
import bysvem.modelo.ItemCompra;
import bysvem.modelo.Jogo;
import bysvem.modelo.Usuario;
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

        // Carrega jogos da empresa do desenvolvedor
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

        // Sorter para ordenação por clique no cabeçalho
        sorter = new TableRowSorter<>(modeloTabela);
        tabelaJogos.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabelaJogos);
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

        atualizarTabela();
        setVisible(true);
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        // Ordena a lista por ID (opcional, pois o sorter já faz a ordenação visual)
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
        // (mesmo código que você já tem, apenas adapte para chamar atualizarTabela() no final)
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

                EntidadeDAO<Jogo> dao = GerenciadorPersistencia.getInstancia().getDAO(Jogo.class);
                int novoId = proximoId(dao);
                Jogo novoJogo = desenvolvedor.criaJogo(novoId, nome, genero, preco, desc);

                dao.salvar(novoJogo);
                dao.persistir("dados/jogos.dat");

                jogosDoDev.add(novoJogo);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Jogo adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Preço inválido. Digite um número.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (PersistenceException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarJogo() {
        int row = tabelaJogos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um jogo na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Converte a linha da view para o modelo (por causa do sorter)
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

        // Painel de edição (igual ao original)
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
            GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
            EntidadeDAO<Jogo> jogoDAO = gp.getDAO(Jogo.class);
            EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);
            EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);

            // Reembolsa usuários que compraram este jogo
            try {
                Compra[] compras = compraDAO.carregarTodos();
                for (Compra compra : compras) {
                    boolean alterou = false;
                    for (ItemCompra item : new ArrayList<>(compra.getItens())) {
                        if (item.getJogo().getId() == jogo.getId()) {
                            Usuario u = compra.getUsuario();
                            u.setSaldo(u.getSaldo() + item.getPrecoPago());
                            compra.getItens().remove(item);
                            alterou = true;
                        }
                    }
                    if (alterou) {
                        compraDAO.atualizar(compra);
                        contaDAO.atualizar(compra.getUsuario());
                    }
                }
                compraDAO.persistir("dados/compras.dat");
                contaDAO.persistir("dados/contas.dat");
            } catch (PersistenceException e) {
                // Nenhuma compra – ignora
            }

            jogoDAO.apagar(jogo.getId());
            jogoDAO.persistir("dados/jogos.dat");

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

    private int proximoId(EntidadeDAO<?> dao) {
        try {
            Object[] entidades = dao.carregarTodos();
            int maior = 0;
            for (Object obj : entidades) {
                int id = ((Entidade) obj).getId();
                if (id > maior) maior = id;
            }
            return maior + 1;
        } catch (PersistenceException e) {
            return 1;
        }
    }
}