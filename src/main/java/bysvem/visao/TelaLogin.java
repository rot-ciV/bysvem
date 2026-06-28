package bysvem.visao;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bysvem.modelo.Compra;
import bysvem.modelo.Conta;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class TelaLogin extends JFrame {

    private JTextField campoEmail;
    private JPasswordField campoSenha;

    public TelaLogin() {
        super();
        setTitle("TelaLogin");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Font fonte = new Font("Arial", Font.BOLD, 20);
        Font fonteLogo = new Font("Arial", Font.BOLD, 70);

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 0, 150, 0);
        JLabel Logo = new JLabel("Bysvem");
        Logo.setFont(fonteLogo);
        painel.add(Logo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel labelEmail = new JLabel("E-mail:");
        labelEmail.setFont(fonte);
        painel.add(labelEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        campoEmail = new JTextField(15);
        campoEmail.setFont(fonte);
        painel.add(campoEmail, gbc);

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

        botaoEntrar.addActionListener(e -> autenticar());
        botaoSair.addActionListener(e -> System.exit(0));
        botaoCriarConta.addActionListener(e -> new TelaCriarConta(TelaLogin.this));
        campoSenha.addActionListener(e -> autenticar());

        setVisible(true);
    }

    private Integer converterSenha(String senha) {
        try {
            return Integer.parseInt(senha);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void autenticar() {
        String email = campoEmail.getText().trim();
        String senhaDigitada = new String(campoSenha.getPassword()).trim();

        if (email.isEmpty() || senhaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha ambos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer senhaInt = converterSenha(senhaDigitada);
        if (senhaInt == null) {
            JOptionPane.showMessageDialog(this, "A senha deve conter apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
            campoSenha.requestFocus();
            return;
        }

        try {
            GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
            EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);
            EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);

            Conta[] contas = contaDAO.carregarTodos();

            Map<Integer, Usuario> mapaUsuarios = new HashMap<>();
            for (Conta c : contas) {
                if (c instanceof Usuario) {
                    Usuario u = (Usuario) c;
                    mapaUsuarios.put(u.getId(), u);
                    u.setCompras(new java.util.ArrayList<>());
                }
            }

            try {
                Compra[] compras = compraDAO.carregarTodos();
                for (Compra compra : compras) {
                    Usuario u = mapaUsuarios.get(compra.getIdUsuario());
                    if (u != null) {
                        compra.setUsuario(u);
                        u.adicionarCompra(compra);
                    }
                }
            } catch (PersistenceException e) {
                // sem compras
            }

            for (Conta c : contas) {
                if (c.getEmail().equals(email) && c.getSenha() == senhaInt) {
                    if (c.getBan()) {
                        JOptionPane.showMessageDialog(this, "Usuário banido! Contate o administrador.",
                                "Acesso negado", JOptionPane.ERROR_MESSAGE);
                        campoSenha.setText("");
                        campoSenha.requestFocus();
                        return;
                    }
                    JOptionPane.showMessageDialog(this, "Login realizado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    SwingUtilities.invokeLater(() -> new Opcoes_Usuario(c));
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.setText("");
            campoSenha.requestFocus();

        } catch (PersistenceException e) {
            JOptionPane.showMessageDialog(this, "Nenhuma conta cadastrada ainda.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }
}