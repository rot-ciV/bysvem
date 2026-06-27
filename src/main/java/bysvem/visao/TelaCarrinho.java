// bysvem/visao/TelaCarrinho.java

package bysvem.visao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import bysvem.modelo.Compra;
import bysvem.modelo.Conta;
import bysvem.modelo.ItemCompra;
import bysvem.modelo.Registro;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaCarrinho extends JDialog {
    private Usuario usuario;
    private DefaultListModel<String> modelo;
    private JList<String> listaItens;
    private JLabel labelTotal;
    private JButton btnFinalizar, btnRemover, btnLimpar, btnContinuar;

    // Caminhos para persistência em arquivo
    private static final String CAMINHO_CONTAS    = "dados/contas.dat";
    private static final String CAMINHO_REGISTROS = "dados/registros.dat";
    private static final String CAMINHO_COMPRAS   = "dados/compras.dat";

    public TelaCarrinho(JFrame parent, Usuario usuario) {
        super(parent, "Carrinho de Compras", true);
        this.usuario = usuario;
        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Seu Carrinho", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        listaItens = new JList<>(modelo);
        listaItens.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(listaItens);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout());
        JPanel painelBotoes   = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnRemover   = new JButton("Remover Selecionado");
        btnLimpar    = new JButton("Limpar Carrinho");
        btnFinalizar = new JButton("Finalizar Compra");
        btnContinuar = new JButton("Continuar Comprando");

        Font fonte = new Font("Arial", Font.BOLD, 16);
        for (JButton b : new JButton[]{btnRemover, btnLimpar, btnFinalizar, btnContinuar}) {
            b.setFont(fonte);
        }

        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnContinuar);

        labelTotal = new JLabel("Total: R$ 0,00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 20));
        labelTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        painelInferior.add(labelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoes, BorderLayout.CENTER);

        panel.add(painelInferior, BorderLayout.SOUTH);
        add(panel);

        btnRemover.addActionListener(e -> removerSelecionado());
        btnLimpar.addActionListener(e -> limparCarrinho());
        btnFinalizar.addActionListener(e -> finalizarCompra());
        btnContinuar.addActionListener(e -> dispose());

        atualizarLista();
        setVisible(true);
    }

    private void atualizarLista() {
        modelo.clear();
        for (ItemCompra item : usuario.getCarrinho()) {
            modelo.addElement(item.getJogo().getNome()
                + " - R$ " + String.format("%.2f", item.getPrecoPago()));
        }
        atualizarTotal();
    }

    private void atualizarTotal() {
        labelTotal.setText(String.format("Total: R$ %.2f", usuario.getTotalCarrinho()));
    }

    private void removerSelecionado() {
        int idx = listaItens.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        usuario.removerDoCarrinho(usuario.getCarrinho().get(idx));
        atualizarLista();
    }

    private void limparCarrinho() {
        if (usuario.getCarrinho().isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remover todos os itens do carrinho?",
            "Limpar Carrinho", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            usuario.limparCarrinho();
            atualizarLista();
        }
    }

    private void finalizarCompra() {
        List<ItemCompra> carrinho = usuario.getCarrinho();

        if (carrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Carrinho vazio!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = usuario.getTotalCarrinho();

        if (usuario.getSaldo() < total) {
            JOptionPane.showMessageDialog(this,
                String.format("Saldo insuficiente!\nSaldo atual: R$ %.2f\nTotal: R$ %.2f",
                    usuario.getSaldo(), total),
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Total da compra: R$ %.2f\nConfirmar compra?", total),
            "Finalizar Compra", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {

            GerenciadorPersistencia gerenciador = GerenciadorPersistencia.getInstancia();

            EntidadeDAO<Conta> contaDAO = gerenciador.getDAO(Conta.class);
            EntidadeDAO<Compra> compraDAO = gerenciador.getDAO(Compra.class);
            EntidadeDAO<Registro> registroDAO = gerenciador.getDAO(Registro.class);

            // 2. Debita o saldo do usuário
            usuario.setSaldo(usuario.getSaldo() - total);

            // 3. Cria a Compra e a salva
            int idCompra = gerarId();
            Compra compra = new Compra(idCompra, usuario, LocalDate.now(), new ArrayList<>(carrinho));
            usuario.adicionarCompra(compra);
            compraDAO.salvar(compra);

            // 4. Cria um Registro por jogo comprado e salva
            for (ItemCompra item : carrinho) {
                Registro registro = new Registro(gerarId(), item.getJogo(), usuario, 0.0);
                registroDAO.salvar(registro);
            }

            try {
                contaDAO.atualizar(usuario);
            } catch (PersistenceException e) {
                // Se o usuario n estava no dao, entao é a primeira compra na sessão
                contaDAO.salvar(usuario);
            }

            contaDAO.persistir(CAMINHO_CONTAS);
            compraDAO.persistir(CAMINHO_COMPRAS);
            registroDAO.persistir(CAMINHO_REGISTROS);

            usuario.limparCarrinho();

            JOptionPane.showMessageDialog(this,
                "Compra finalizada com sucesso!\nTotal pago: R$ " + String.format("%.2f", total),
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this,
                "Erro de persistência: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao finalizar compra: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int gerarId() {
        return (int) (Math.random() * 100000);
    }
}