package bysvem.visao;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bysvem.modelo.Conta;
import bysvem.persistencia.PersistenceException;

public class TelaCriarConta extends JDialog {

    private JTextField campoNome;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JTextField campoEmail;

    public TelaCriarConta(JFrame parent) {
        super(parent, "Criar Conta", true);
        setSize(1000, 800);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Font fonte = new Font("Arial", Font.BOLD, 16);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titulo = new JLabel("Criar Nova Conta");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        painel.add(titulo, gbc);

        gbc.gridwidth = 1;

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelNome = new JLabel("Nome de usuário:*");
        labelNome.setFont(fonte);
        painel.add(labelNome, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoNome = new JTextField(15);
        campoNome.setFont(fonte);
        painel.add(campoNome, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(fonte);
        painel.add(labelEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoEmail = new JTextField(15);
        campoEmail.setFont(fonte);
        painel.add(campoEmail, gbc);

        // Senha
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelSenha = new JLabel("Senha:* (apenas números)");
        labelSenha.setFont(fonte);
        painel.add(labelSenha, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoSenha = new JPasswordField(15);
        campoSenha.setFont(fonte);
        painel.add(campoSenha, gbc);

        // Confirmar Senha
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel labelConfirmar = new JLabel("Confirmar senha:*");
        labelConfirmar.setFont(fonte);
        painel.add(labelConfirmar, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoConfirmarSenha = new JPasswordField(15);
        campoConfirmarSenha.setFont(fonte);
        painel.add(campoConfirmarSenha, gbc);

        // Botões
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel painelBotoes = new JPanel();
        JButton botaoCadastrar = new JButton("Cadastrar");
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCadastrar.setFont(fonte);
        botaoCancelar.setFont(fonte);

        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoCancelar);
        painel.add(painelBotoes, gbc);

        add(painel);

        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoCancelar.addActionListener(e -> dispose());
        campoConfirmarSenha.addActionListener(e -> cadastrar());

        setVisible(true);
    }

    private void cadastrar() {
        try {
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            String confirmar = new String(campoConfirmarSenha.getPassword()).trim();

            if (nome.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
                throw new IllegalArgumentException("Nome de usuário e senha são obrigatórios!");
            }
            if (!senha.equals(confirmar)) {
                throw new IllegalArgumentException("As senhas não coincidem!");
            }
            int senhaInt;
            try {
                senhaInt = Integer.parseInt(senha);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("A senha deve conter apenas números.");
            }

            Conta.criarConta(nome, email, senhaInt);

            JOptionPane.showMessageDialog(this,
                    "Conta criada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);

            String msg = e.getMessage();
            if (msg.contains("senha") || msg.contains("coincidem")) {
                campoSenha.setText("");
                campoConfirmarSenha.setText("");
                campoSenha.requestFocus();
            } else if (msg.contains("Usuário") || msg.contains("E-mail")) {
                campoNome.setText("");
                campoEmail.setText("");
                campoNome.requestFocus();
            }
        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}