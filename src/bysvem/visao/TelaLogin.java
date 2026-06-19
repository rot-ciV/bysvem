package bysvem.visao;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bysvem.modelo.Conta;
import bysvem.modelo.Gerenciador;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;      // mudança: campo para email
    private JPasswordField campoSenha;

    public TelaLogin() {
        super();
        setTitle("TelaLogin");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font fonte = new Font("Arial", Font.BOLD, 20);
        Font fonteLogo = new Font("Arial", Font.BOLD, 40);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Logo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel Logo = new JLabel("Bysvem");
        Logo.setFont(fonteLogo);
        painel.add(Logo, gbc);

        gbc.gridwidth = 1;

        // Email (em vez de Usuário)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(fonte);
        painel.add(labelEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoEmail = new JTextField(15);  // novo campo
        campoEmail.setFont(fonte);
        painel.add(campoEmail, gbc);

        // Senha (mantido)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(fonte);
        painel.add(labelSenha, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoSenha = new JPasswordField(15);
        campoSenha.setFont(fonte);
        painel.add(campoSenha, gbc);

        // Botões (inalterado)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel();
        JButton botaoEntrar = new JButton("Entrar");
        JButton botaoCriarConta = new JButton("Criar Conta");
        JButton botaoSair = new JButton("Sair");

        botaoEntrar.setFont(fonte);
        botaoCriarConta.setFont(fonte);
        botaoSair.setFont(fonte);

        painelBotoes.add(botaoEntrar);
        painelBotoes.add(botaoCriarConta);
        painelBotoes.add(botaoSair);
        painel.add(painelBotoes, gbc);

        add(painel);

        // Ações
        botaoEntrar.addActionListener(e -> autenticar());
        botaoSair.addActionListener(e -> System.exit(0));
        botaoCriarConta.addActionListener(e -> new TelaCriarConta(TelaLogin.this));
        campoSenha.addActionListener(e -> autenticar());

        setVisible(true);
    }

    // Método auxiliar para converter senha String para int
    private Integer converterSenha(String senha) {
        try {
            return Integer.parseInt(senha);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void autenticar() {
        String email = campoEmail.getText().trim();       // agora usa email
        String senhaDigitada = new String(campoSenha.getPassword()).trim();

        if (email.isEmpty() || senhaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Preencha ambos os campos!", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Converte a senha
        Integer senhaInt = converterSenha(senhaDigitada);
        if (senhaInt == null) {
            JOptionPane.showMessageDialog(this,
                    "A senha deve conter apenas números.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
            campoSenha.requestFocus();
            return;
        }

        // Carrega contas via Gerenciador
        ArrayList<Conta> contas = Gerenciador.carregaContas();
        if (contas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar contas. Verifique o arquivo.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Compara o email (campo c.getEmail()) com o digitado
        for (Conta c : contas) {
            if (c.getEmail().equals(email) && c.getSenha() == senhaInt) {
                if (c.getBan()) {
                    JOptionPane.showMessageDialog(this,
                            "Usuário banido! Contate o administrador.",
                            "Acesso negado", JOptionPane.ERROR_MESSAGE);
                    campoSenha.setText("");
                    campoSenha.requestFocus();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Login realizado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    SwingUtilities.invokeLater(() -> new Opcoes_Usuario(c));
                    return;
                }
            }
        }

        // Não encontrou
        JOptionPane.showMessageDialog(this,
                "E-mail ou senha inválidos!", "Erro",
                JOptionPane.ERROR_MESSAGE);
        campoSenha.setText("");
        campoSenha.requestFocus();
    }

    public static void main(String[] args) {
        new TelaLogin();
    }
}