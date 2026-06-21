package bysvem.visao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dialog;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import bysvem.modelo.Compra;
import bysvem.modelo.Conta;
import bysvem.modelo.Gerenciador;
import bysvem.modelo.ItemCompra;
import bysvem.modelo.Jogo;
import bysvem.modelo.TipoAquisicao;
import bysvem.modelo.Usuario;
import bysvem.modelo.Registro;

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

        // Verifica se o usuário já possui o jogo
        boolean jaPossui = usuarioLogado != null &&
                usuarioLogado.biblioteca().stream().anyMatch(j -> j.getId() == jogo.getId());

        if (mostrarAdquirir && usuarioLogado != null && !jaPossui) {
            // Botão para adicionar ao carrinho (em vez de comprar direto)
            JButton btnAdicionar = new JButton("Adicionar ao Carrinho");
            btnAdicionar.setFont(Estilos.FONTE_BOTAO_DETALHE);
            btnAdicionar.addActionListener(e -> {
                // Verifica se já está no carrinho
                if (usuarioLogado.jogoNoCarrinho(jogo)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Este jogo já está no seu carrinho!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Cria um ItemCompra para o carrinho
                int idItem = gerarId();
                ItemCompra item = new ItemCompra(idItem, jogo, TipoAquisicao.COMPRA_DEFINITIVA, jogo.getPreco());
                usuarioLogado.adicionarAoCarrinho(item);

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

        // Botão Voltar (sempre presente)
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setFont(Estilos.FONTE_BOTAO_DETALHE);
        btnVoltar.addActionListener(e -> dialog.dispose());
        painelBotoes.add(btnVoltar);

        panel.add(painelBotoes, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    private static boolean realizarCompra(Usuario usuario, Jogo jogo, Component parent) {
        // Verifica se já possui
        if (usuario.biblioteca().stream().anyMatch(j -> j.getId() == jogo.getId())) {
            JOptionPane.showMessageDialog(parent,
                    "Você já possui este jogo!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Verifica saldo
        if (usuario.getSaldo() < jogo.getPreco()) {
            JOptionPane.showMessageDialog(parent,
                    "Saldo insuficiente!\nSaldo atual: R$" + String.format("%.2f", usuario.getSaldo()),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Cria ItemCompra
        int idItem = gerarId();
        ItemCompra item = new ItemCompra(idItem, jogo, TipoAquisicao.COMPRA_DEFINITIVA, jogo.getPreco());
        List<ItemCompra> itens = new ArrayList<>();
        itens.add(item);
        int idCompra = gerarId();
        Compra compra = new Compra(idCompra, usuario, LocalDate.now(), itens);

        int idReg = gerarId();
        Registro registro = new Registro(idReg, jogo, usuario, 0.0);

        // --- Atualiza o objeto USUARIO original (a interface usa este) ---
        usuario.setSaldo(usuario.getSaldo() - jogo.getPreco());
        usuario.adicionarCompra(compra);

        // --- Carrega dados para persistência ---
        ArrayList<Conta> contas = Gerenciador.carregaContas();
        ArrayList<Jogo> jogos = Gerenciador.carregaJogos();
        Gerenciador.carregarCompras(contas, jogos); // preenche as compras nas contas carregadas
        Gerenciador.carregarRegistros(jogos, contas); // carrega registros na lista estática

        // --- Substitui o usuário na lista de contas pelo objeto atualizado (usuario) ---
        boolean encontrado = false;
        for (int i = 0; i < contas.size(); i++) {
            if (contas.get(i).getId() == usuario.getId()) {
                contas.set(i, usuario); // agora a lista tem o usuário com a nova compra
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            JOptionPane.showMessageDialog(parent, "Erro: usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // --- Persiste tudo ---
        Gerenciador.salvarContas(contas);
        Gerenciador.salvarTodasCompras(contas);
        Gerenciador.salvarRegistro(registro); // a lista estática já tem os registros antigos

        return true;
    }

    // Gerador de ID (substitua pelo seu método)
    private static int gerarId() {
        return (int) (Math.random() * 100000);
    }

    // Método auxiliar para adicionar linhas no painel
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