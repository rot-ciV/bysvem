package bysvem.visao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bysvem.modelo.Conta;
import bysvem.modelo.Desenvolvedor;
import bysvem.modelo.Operador;
import bysvem.modelo.Usuario;

public class Opcoes_Usuario extends JFrame {

    private Conta usuarioLogado;

    public Opcoes_Usuario(Conta conta) {
        this.usuarioLogado = conta;
        setTitle("Loja Bysvem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titulo = new JLabel(" Loja Bysvem ");
        titulo.setFont(new Font("Arial", Font.BOLD, 55));
        gbc.insets = new Insets(25, 0, 45, 0);
        painel.add(titulo, gbc);

        gbc.gridy = 1;
        painel.add(new JLabel(" "), gbc);

        JPanel opcoesPanel = new JPanel();
        opcoesPanel.setLayout(new BoxLayout(opcoesPanel, BoxLayout.Y_AXIS));
        opcoesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ---- ORDEM DOS BOTÕES ----
        java.util.List<String> ordemBotoes = new java.util.ArrayList<>();

        // 1. Jogos Disponíveis (sempre)
        ordemBotoes.add(" Jogos Disponíveis ");

        // 2. Biblioteca e Carrinho (apenas Usuário)
        if (usuarioLogado instanceof Usuario) {
            ordemBotoes.add(" Biblioteca ");
            ordemBotoes.add(" Carrinho ");
        }

        // 3. Configurações (sempre)
        ordemBotoes.add(" Configurações ");

        // 4. Botões específicos de Desenvolvedor
        if (usuarioLogado instanceof Desenvolvedor) {
            ordemBotoes.add(" Gerenciar Jogos ");
        }

        // 5. Botões específicos de Operador
        if (usuarioLogado instanceof Operador) {
            ordemBotoes.add(" Gerenciar Usuários ");
            ordemBotoes.add(" Gerenciar Jogos (Admin) ");
            ordemBotoes.add(" Visualizar Compras "); // NOVO
        }

        // 6. Sair (sempre no final)
        ordemBotoes.add(" Sair ");

        // Cria os botões na ordem definida
        for (String texto : ordemBotoes) {
            JButton botao = criarBotao(texto);
            opcoesPanel.add(botao);
            opcoesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        painel.add(opcoesPanel, gbc);

        add(painel);
        setVisible(true);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 24)); // TAMANHO PADRÃO
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);
        botao.setMaximumSize(new Dimension(420, 60));
        botao.setPreferredSize(new Dimension(420, 60));

        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String comando = botao.getText().trim();
                switch (comando) {
                    case "Jogos Disponíveis":
                        if (usuarioLogado instanceof Usuario) {
                            new Jogos_Disponiveis((Usuario) usuarioLogado);
                        } else {
                            new Jogos_Disponiveis(null);
                        }
                        break;
                    case "Biblioteca":
                        new Biblioteca(usuarioLogado);
                        break;
                    case "Carrinho":
                        if (usuarioLogado instanceof Usuario) {
                            new TelaCarrinho(Opcoes_Usuario.this, (Usuario) usuarioLogado);
                        }
                        break;
                    case "Configurações":
                        new Configuracoes(Opcoes_Usuario.this, usuarioLogado);
                        break;
                    case "Gerenciar Jogos":
                        if (usuarioLogado instanceof Desenvolvedor) {
                            new TelaGerenciarJogos(Opcoes_Usuario.this, (Desenvolvedor) usuarioLogado);
                        }
                        break;
                    case "Gerenciar Usuários":
                        if (usuarioLogado instanceof Operador) {
                            new TelaGerenciarUsuarios(Opcoes_Usuario.this);
                        }
                        break;
                    case "Gerenciar Jogos (Admin)":
                        if (usuarioLogado instanceof Operador) {
                            new TelaGerenciarJogosAdmin(Opcoes_Usuario.this);
                        }
                        break;
                    case "Visualizar Compras": // NOVO
                        if (usuarioLogado instanceof Operador) {
                            new TelaVisualizarCompras(Opcoes_Usuario.this);
                        }
                        break;
                    case "Sair":
                        sair();
                        break;
                }
            }
        });
        return botao;
    }

    private void sair() {
        Object[] opcoes = {"Sim", "Voltar"};
        int resposta = JOptionPane.showOptionDialog(
                this,
                "     Tem certeza que deseja sair?",
                "Confirmação",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                opcoes,
                opcoes[0]
        );
        if (resposta == 0) {
            dispose();
            SwingUtilities.invokeLater(() -> new TelaLogin());
        }
    }
}