package bysvem.visao;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

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

import bysvem.modelo.ItemCompra;
import bysvem.modelo.Usuario;
import bysvem.persistencia.PersistenceException;

public class TelaCarrinho extends JDialog {
    private Usuario usuario;
    private DefaultListModel<String> modelo;
    private JList<String> listaItens;
    private JLabel labelTotal;
    private JButton btnFinalizar, btnRemover, btnLimpar, btnContinuar;

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
        double total = usuario.getTotalCarrinho();

        try {
            usuario.finalizarCompra();
            JOptionPane.showMessageDialog(this,
                    "Compra finalizada com sucesso!\nTotal pago: R$ " + String.format("%.2f", total),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // fecha a tela do carrinho
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
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
}