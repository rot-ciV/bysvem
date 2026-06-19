package bysvem.visao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bysvem.modelo.Conta;
import bysvem.modelo.Usuario;

public class Opcoes_Usuario extends JFrame {

    private Conta usuarioLogado;

    public Opcoes_Usuario(Conta conta) {
        this.usuarioLogado = conta; 
        setTitle("Loja Bysvem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Painel principal com GridBagLayout
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titulo = new JLabel("========= Loja Bysvem ==========");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painel.add(titulo, gbc);

        // Espaço vazio (linha 1)
        gbc.gridy = 1;
        painel.add(new JLabel(" "), gbc);

        // Painel das opções (BoxLayout vertical)
        JPanel opcoesPanel = new JPanel();
        opcoesPanel.setLayout(new BoxLayout(opcoesPanel, BoxLayout.Y_AXIS));
        opcoesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] opcoes = {
            "1 - Jogos Disponíveis",
            "2 - Biblioteca",
            "3 - Configurações",
            "4 - Sair"
        };

        // Adiciona os botões com seus listeners
        for (String texto : opcoes) {
            JButton botao = new JButton(texto);
            botao.setFont(new Font("Arial", Font.PLAIN, 16));
            botao.setAlignmentX(Component.CENTER_ALIGNMENT);
            botao.setMaximumSize(new Dimension(280, 40));
            botao.setPreferredSize(new Dimension(280, 40));

            // Ação de cada botão
            botao.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String comando = botao.getText();
                    switch (comando) {
                        case "1 - Jogos Disponíveis":
                            new Jogos_Disponiveis();
                            break;
                        case "2 - Biblioteca":
                            new Biblioteca(usuarioLogado);
                            break;
                        case "3 - Configurações":
                            new Configuracoes(Opcoes_Usuario.this, usuarioLogado);
                            break;
                        case "4 - Sair":
                            sair();
                            break;
                    }
                }
            });

            opcoesPanel.add(botao);
            opcoesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        painel.add(opcoesPanel, gbc);

        // Adiciona o painel à janela
        add(painel);

        // Exibe a janela
        setVisible(true);
    }

    private void sair() {
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja sair?",
            "Confirmação",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new TelaLogin());
        }
        // Se for NO, não faz nada
    }

    public static void main(String[] args) {
        // Cria uma conta fictícia para teste
        Conta dummy = new Usuario(999, "Teste", 1234, "teste@teste.com", 0.0, false);
        SwingUtilities.invokeLater(() -> new Opcoes_Usuario(dummy));
    }
}