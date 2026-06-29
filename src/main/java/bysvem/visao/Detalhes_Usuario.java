package bysvem.visao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import bysvem.modelo.Jogo;
import bysvem.modelo.Usuario;

public class Detalhes_Usuario {

    private Detalhes_Usuario() {}

    public static void exibirDetalhes(Component parent, Jogo jogo, boolean mostrarAdquirir, Usuario usuarioLogado) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Detalhes do Jogo", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(1000, 800);
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fonteCategoria = Estilos.FONTE_CATEGORIA_DETALHE;
        Font fonteValor = Estilos.FONTE_VALOR_DETALHE;
        Font fonteDesc = Estilos.FONTE_DESCRICAO;

        int linha = 0;

        adicionarLinha(panel, gbc, linha++, "Nome:", jogo.getNome(), fonteCategoria, fonteValor);
        adicionarLinha(panel, gbc, linha++, "Gênero:", jogo.getGenero(), fonteCategoria, fonteValor);
        adicionarLinha(panel, gbc, linha++, "Desenvolvedora:", jogo.getDesenvolvedora(), fonteCategoria, fonteValor);
        String precoStr = (jogo.getPreco() <= 0.1) ? "Gratuito" : "R$ " + String.format("%.2f", jogo.getPreco());
        adicionarLinha(panel, gbc, linha++, "Preço:", precoStr, fonteCategoria, fonteValor);

        // Descrição
        gbc.gridx = 0;
        gbc.gridy = linha++;
        gbc.gridwidth = 2;
        JLabel lblDesc = new JLabel("Descrição:");
        lblDesc.setFont(fonteCategoria);
        panel.add(lblDesc, gbc);

        gbc.gridy = linha++;
        JTextArea descArea = new JTextArea(jogo.getDesc());
        descArea.setFont(fonteDesc);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(panel.getBackground());
        JScrollPane scrollDesc = new JScrollPane(descArea);
        scrollDesc.setPreferredSize(new Dimension(600, 100));
        panel.add(scrollDesc, gbc);

        // ======== PAINEL DE BOTÕES ========
        gbc.gridy = linha++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        boolean jaPossui = usuarioLogado != null &&
                usuarioLogado.biblioteca().stream().anyMatch(j -> j.getId() == jogo.getId());

        if (mostrarAdquirir && usuarioLogado != null && !jaPossui) {
            // Botão para adicionar ao carrinho (em vez de comprar direto)
            JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
            btnAdicionar.setFont(Estilos.FONTE_BOTAO_DETALHE);
            btnAdicionar.addActionListener(e -> {
                if (usuarioLogado.jogoNoCarrinho(jogo)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Este jogo já está no seu carrinho!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                usuarioLogado.adicionarAoCarrinho(jogo);

                JOptionPane.showMessageDialog(dialog,
                        "Jogo adicionado ao carrinho!",
                        "Carrinho", JOptionPane.INFORMATION_MESSAGE);
            });
            painelBotoes.add(btnAdicionar);
        } else if (jaPossui) {
            JLabel lblPossui = new JLabel("✓ Já adquirido");
            lblPossui.setFont(new Font("Arial", Font.BOLD, 22));
            lblPossui.setForeground(Color.GREEN);
            painelBotoes.add(lblPossui);
        }

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(Estilos.FONTE_BOTAO_DETALHE);
        btnVoltar.addActionListener(e -> dialog.dispose());
        painelBotoes.add(btnVoltar);

        panel.add(painelBotoes, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private static void adicionarLinha(JPanel panel, GridBagConstraints gbc, int linha, String rotulo, String valor,
                                       Font fonteRotulo, Font fonteValor) {
        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel lbl = new JLabel(rotulo);
        lbl.setFont(fonteRotulo);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JLabel val = new JLabel(valor);
        val.setFont(fonteValor);
        panel.add(val, gbc);
    }
}