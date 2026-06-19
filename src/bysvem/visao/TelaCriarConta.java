package bysvem.visao;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bysvem.modelo.Conta;
import bysvem.modelo.Gerenciador;
import bysvem.modelo.Usuario;

public class TelaCriarConta extends JDialog {

    private JTextField campoNome;
    private JPasswordField campoSenha;
    private JPasswordField campoConfirmarSenha;
    private JTextField campoEmail;

    public TelaCriarConta(JFrame parent) {
        super(parent, "Criar Conta", true);
        setSize(500, 350);
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

        // Ações
        botaoCadastrar.addActionListener(e -> cadastrar());
        botaoCancelar.addActionListener(e -> dispose());
        campoConfirmarSenha.addActionListener(e -> cadastrar());

        setVisible(true);
    }

    // Método auxiliar para converter senha String para int (lança exceção)
    private int converterSenha(String senha) throws IllegalArgumentException {
        try {
            return Integer.parseInt(senha);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("A senha deve conter apenas números.");
        }
    }

    private void cadastrar() {
        try {
            // 1. Captura e valida os dados
            String nome = campoNome.getText().trim();
            String email = campoEmail.getText().trim();
            String senha = new String(campoSenha.getPassword()).trim();
            String confirmar = new String(campoConfirmarSenha.getPassword()).trim();

            // Validações obrigatórias
            if (nome.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
                throw new IllegalArgumentException("Nome de usuário e senha são obrigatórios!");
            }

            if (!senha.equals(confirmar)) {
                throw new IllegalArgumentException("As senhas não coincidem!");
            }

            // Converte a senha (lança exceção se não for número)
            int senhaInt = converterSenha(senha);

            // 2. Carrega contas existentes
            ArrayList<Conta> contas = Gerenciador.carregaContas();
            if (contas == null || contas.isEmpty()) {
                throw new IllegalStateException("Erro ao carregar contas. Verifique o arquivo.");
            }

            // 3. Verifica duplicidade de nome e calcula maior ID
            int maiorId = 0;
            for (Conta c : contas) {
                if (c.getNome().equalsIgnoreCase(nome)) {
                    throw new IllegalArgumentException("Usuário já cadastrado!");
                }
                if (c.getId() > maiorId) {
                    maiorId = c.getId();
                }
            }

            // 4. Cria e salva a nova conta
            int novoId = maiorId + 1;
            Usuario novaConta = new Usuario(novoId, nome, senhaInt, email, 0.0, false);
            contas.add(novaConta);
            Gerenciador.salvarContas(contas);

            // Sucesso
            JOptionPane.showMessageDialog(this,
                    "Conta criada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Exibe a mensagem de erro para o usuário
            JOptionPane.showMessageDialog(this,
                    e.getMessage(), "Erro",
                    JOptionPane.ERROR_MESSAGE);
            // Limpa os campos adequados conforme o erro
            if (e.getMessage().contains("senha") || e.getMessage().contains("coincidem")) {
                campoSenha.setText("");
                campoConfirmarSenha.setText("");
                campoSenha.requestFocus();
            } else if (e.getMessage().contains("Usuário já cadastrado")) {
                campoNome.setText("");
                campoNome.requestFocus();
            }
            // Para outros erros, mantém os campos
        } catch (Exception e) {
            // Captura qualquer outra exceção inesperada
            JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro inesperado: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}