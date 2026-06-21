package bysvem.visao;

import bysvem.modelo.Conta;
import bysvem.modelo.Jogo;
import bysvem.modelo.Usuario;
import bysvem.modelo.Gerenciador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Biblioteca extends JFrame {
    private Conta usuarioLogado;
    private ArrayList<Jogo> jogosDoUsuario;
    private JList<String> listaNomes;
    private DefaultListModel<String> modeloLista;

    public Biblioteca(Conta usuario) {
        this.usuarioLogado = usuario;
        setTitle("Minha Biblioteca");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        if (usuario instanceof Usuario) {
            jogosDoUsuario = ((Usuario) usuario).biblioteca();
        } else {
            jogosDoUsuario = new ArrayList<>();
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Biblioteca de " + usuarioLogado.getNome(), SwingConstants.CENTER);
        titulo.setFont(Estilos.FONTE_TITULO_TELA);
        panel.add(titulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        for (Jogo j : jogosDoUsuario) modeloLista.addElement(j.getNome());
        listaNomes = new JList<>(modeloLista);
        listaNomes.setFont(Estilos.FONTE_LISTA);
        listaNomes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(listaNomes);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnDetalhes = new JButton("Ver detalhes");
        JButton btnVoltar = new JButton("Voltar");

        btnDetalhes.setFont(Estilos.FONTE_BOTAO_PRINCIPAL);
        btnVoltar.setFont(Estilos.FONTE_BOTAO_PRINCIPAL);

        panelBotoes.add(btnDetalhes);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);
        add(panel);

        btnDetalhes.addActionListener(e -> {
            int idx = listaNomes.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um jogo na lista.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Jogo jogoSelecionado = jogosDoUsuario.get(idx);
            Usuario user = (usuarioLogado instanceof Usuario) ? (Usuario) usuarioLogado : null;
            Detalhes_Usuario.exibirDetalhes(this, jogoSelecionado, false, user);
        });

        listaNomes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listaNomes.getSelectedIndex();
                    if (idx != -1) {
                        Jogo jogoSelecionado = jogosDoUsuario.get(idx);
                        Usuario user = (usuarioLogado instanceof Usuario) ? (Usuario) usuarioLogado : null;
                        Detalhes_Usuario.exibirDetalhes(Biblioteca.this, jogoSelecionado, false, user);
                    }
                }
            }
        });

        btnVoltar.addActionListener(e -> dispose());
        setVisible(true);
    }
}