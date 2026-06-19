package bysvem.visao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Dialog;
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

public class Detalhes_Usuario {

    private Detalhes_Usuario() {} // impede instanciação

    public static void exibirDetalhes(Component parent, Jogo jogo, boolean mostrarAdquirir) {
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
        String precoStr;
        if (jogo.getPreco() <= 0.1) {
            precoStr = "Gratuito";
        } else {
            precoStr = "R$ " + String.format("%.2f", jogo.getPreco());
        }
        adicionarLinha(panel, gbc, linha++, "Preço:", precoStr, fonteCategoria, fonteValor);

        // Descrição (ocupa 2 colunas)
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

        // Botões
        gbc.gridy = linha++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        if (mostrarAdquirir) {
            JButton btnAdquirir = new JButton("Adquirir");
            btnAdquirir.setFont(Estilos.FONTE_BOTAO_DETALHE);
            btnAdquirir.addActionListener(e -> {
                // Lógica de aquisição
                JOptionPane.showMessageDialog(dialog, "Jogo adquirido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Talvez atualizar a biblioteca
            });
            painelBotoes.add(btnAdquirir);
        }
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(Estilos.FONTE_BOTAO_DETALHE);
        btnVoltar.addActionListener(e -> dialog.dispose());
        painelBotoes.add(btnVoltar);

        panel.add(painelBotoes, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Método auxiliar
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