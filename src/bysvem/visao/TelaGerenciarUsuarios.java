// bysvem/visao/TelaGerenciarUsuarios.java

package bysvem.visao;

import bysvem.modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TelaGerenciarUsuarios extends JDialog {
    private ArrayList<Conta> contas;
    private DefaultListModel<String> modelo;
    private JList<String> listaContas;

    public TelaGerenciarUsuarios(JFrame parent) {
        super(parent, "Gerenciar Usuários", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        contas = Gerenciador.carregaContas();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Usuários e Desenvolvedores", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titulo, BorderLayout.NORTH);

        modelo = new DefaultListModel<>();
        atualizarLista();

        listaContas = new JList<>(modelo);
        listaContas.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(listaContas);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnBanir = new JButton("Banir/Desbanir");
        JButton btnVoltar = new JButton("Voltar");

        Font fonte = new Font("Arial", Font.BOLD, 16);
        btnBanir.setFont(fonte);
        btnVoltar.setFont(fonte);

        painelBotoes.add(btnBanir);
        painelBotoes.add(btnVoltar);
        panel.add(painelBotoes, BorderLayout.SOUTH);

        add(panel);

        btnBanir.addActionListener(e -> banirDesbanir());
        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void atualizarLista() {
        modelo.clear();
        for (Conta c : contas) {
            if (c instanceof Usuario || c instanceof Desenvolvedor) {
                String tipo = (c instanceof Usuario) ? "Usuário" : "Desenvolvedor";
                String status = c.getBan() ? "[BANIDO] " : "";
                modelo.addElement(status + c.getNome() + " (" + tipo + ") - ID: " + c.getId());
            }
        }
    }

    private void banirDesbanir() {
        int idx = listaContas.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Conta selecionada = null;
        int count = 0;
        for (Conta c : contas) {
            if (c instanceof Usuario || c instanceof Desenvolvedor) {
                if (count == idx) {
                    selecionada = c;
                    break;
                }
                count++;
            }
        }

        if (selecionada == null) return;

        String acao = selecionada.getBan() ? "desbanir" : "banir";
        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja " + acao + " o usuário " + selecionada.getNome() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            selecionada.setBan(!selecionada.getBan());
            // Salva diretamente
            Gerenciador.salvarContas(contas);
            atualizarLista();
            JOptionPane.showMessageDialog(this,
                    "Usuário " + (selecionada.getBan() ? "banido" : "desbanido") + " com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}