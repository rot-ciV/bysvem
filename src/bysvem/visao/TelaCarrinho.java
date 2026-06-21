// bysvem/visao/TelaCarrinho.java

package bysvem.visao;

import bysvem.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        // Lista de itens
        modelo = new DefaultListModel<>();
        listaItens = new JList<>(modelo);
        listaItens.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(listaItens);
        panel.add(scroll, BorderLayout.CENTER);

        // --- Painel inferior (deve ser criado antes de atualizar a lista) ---
        JPanel painelInferior = new JPanel(new BorderLayout());
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnRemover = new JButton("Remover Selecionado");
        btnLimpar = new JButton("Limpar Carrinho");
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

        // Inicializa labelTotal ANTES de chamar atualizarLista
        labelTotal = new JLabel("Total: R$ 0,00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 20));
        labelTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        painelInferior.add(labelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoes, BorderLayout.CENTER);

        panel.add(painelInferior, BorderLayout.SOUTH);
        add(panel);

        // Listeners
        btnRemover.addActionListener(e -> removerSelecionado());
        btnLimpar.addActionListener(e -> limparCarrinho());
        btnFinalizar.addActionListener(e -> finalizarCompra());
        btnContinuar.addActionListener(e -> dispose());

        // AGORA, com labelTotal já criado, podemos atualizar a lista
        atualizarLista();

        setVisible(true);
    }

    private void atualizarLista() {
        modelo.clear();
        for (ItemCompra item : usuario.getCarrinho()) {
            modelo.addElement(item.getJogo().getNome() + " - R$ " + String.format("%.2f", item.getPrecoPago()));
        }
        atualizarTotal(); // labelTotal já existe
    }

    private void atualizarTotal() {
        double total = usuario.getTotalCarrinho();
        labelTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void removerSelecionado() {
        int idx = listaItens.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um item para remover.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ItemCompra item = usuario.getCarrinho().get(idx);
        usuario.removerDoCarrinho(item);
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
                    "Carrinho vazio!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
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

        // Processa a compra
        try {
            // Debita o saldo
            usuario.setSaldo(usuario.getSaldo() - total);

            // Cria uma única compra com todos os itens
            int idCompra = gerarId();
            Compra compra = new Compra(idCompra, usuario, LocalDate.now(), new ArrayList<>(carrinho));

            // Adiciona ao histórico
            usuario.adicionarCompra(compra);

            // Cria registros para cada jogo
            for (ItemCompra item : carrinho) {
                int idReg = gerarId();
                Registro registro = new Registro(idReg, item.getJogo(), usuario, 0.0);
                Gerenciador.salvarRegistro(registro);
            }

            // Persiste contas e compras
            ArrayList<Conta> contas = Gerenciador.carregaContas();
            ArrayList<Jogo> jogos = Gerenciador.carregaJogos(); // não precisa carregar, mas para salvar
            Gerenciador.carregarCompras(contas, jogos); // atualiza as listas de compras

            // Atualiza o usuário na lista
            for (int i = 0; i < contas.size(); i++) {
                if (contas.get(i).getId() == usuario.getId()) {
                    contas.set(i, usuario);
                    break;
                }
            }

            Gerenciador.salvarContas(contas);
            Gerenciador.salvarTodasCompras(contas);

            // Limpa o carrinho
            usuario.limparCarrinho();

            JOptionPane.showMessageDialog(this,
                    "Compra finalizada com sucesso!\n" +
                            "Total pago: R$ " + String.format("%.2f", total),
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            dispose(); // fecha a tela do carrinho

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao finalizar compra: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private int gerarId() {
        return (int) (Math.random() * 100000);
    }
}