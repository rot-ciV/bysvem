package visao;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaLogin extends JFrame{

    public TelaLogin(){
        super();
        setTitle("TelaLogin");
        setSize(500,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font fonte = new Font("Arial", Font.BOLD, 20);
        Font fonteLogo = new Font("Arial", Font.BOLD, 40);

        // Painel com GridBagLayout
        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        //Logo Bysvem no meio
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel Logo = new JLabel("Bysvem");
        Logo.setFont(fonteLogo);
        painel.add(Logo, gbc);

        gbc.gridwidth = 1;
        
        // Textinho de usuário
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelUsuario = new JLabel("Usuário:");
        labelUsuario.setFont(fonte);
        painel.add(labelUsuario, gbc);

        // Caixinha pra escrever o usuário
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JTextField campoUsuario = new JTextField(15);
        campoUsuario.setFont(fonte);
        painel.add(campoUsuario, gbc);

        // Textinho de senha
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(fonte);
        painel.add(labelSenha, gbc);

        // Caixinha pra escrever a senha
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        JPasswordField campoSenha = new JPasswordField(15);
        campoSenha.setFont(fonte);
        painel.add(campoSenha, gbc);

        // Botões
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel();
        JButton botaoEntrar = new JButton("Entrar");
        botaoEntrar.setFont(fonte);
        painelBotoes.add(botaoEntrar);
        painel.add(painelBotoes, gbc);

        add(painel);

        // Exibe a janela
        setVisible(true);
    }

    public static void main(String[] args) {
        new TelaLogin();
    }
}