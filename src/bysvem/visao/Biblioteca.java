package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import bysvem.modelo.Conta;
import bysvem.modelo.Gerenciador;
import bysvem.modelo.Jogo;
import bysvem.modelo.Registro;

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

        ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
        ArrayList<Conta> todasContas = Gerenciador.carregaContas();
        ArrayList<Registro> registros = Gerenciador.CarregaRegistros(todosJogos, todasContas);

        jogosDoUsuario = new ArrayList<>();
        for (Registro reg : registros) {
            if (reg.getConta().getId() == usuarioLogado.getId()) {
                Jogo jogo = reg.getJogo();
                if (jogo != null) jogosDoUsuario.add(jogo);
            }
        }

        if (jogosDoUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sua biblioteca está vazia.\nAdquira jogos na loja!",
                    "Biblioteca Vazia", JOptionPane.INFORMATION_MESSAGE);
        }

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Biblioteca de " + usuarioLogado.getNome(), SwingConstants.CENTER);
        titulo.setFont(Estilos.FONTE_TITULO_TELA);   // padronizado
        panel.add(titulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        for (Jogo j : jogosDoUsuario) modeloLista.addElement(j.getNome());
        listaNomes = new JList<>(modeloLista);
        listaNomes.setFont(Estilos.FONTE_LISTA);      // mesma fonte da loja
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

        // Ação do botão Detalhes
        btnDetalhes.addActionListener(e -> {
            int idx = listaNomes.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(this,
                        "Selecione um jogo na lista.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Jogo jogoSelecionado = jogosDoUsuario.get(idx);
            Detalhes_Usuario.exibirDetalhes(this, jogoSelecionado, false);
        });

        // Duplo clique
        listaNomes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listaNomes.getSelectedIndex();
                    if (idx != -1) {
                        Jogo jogoSelecionado = jogosDoUsuario.get(idx);
                        Detalhes_Usuario.exibirDetalhes(Biblioteca.this, jogoSelecionado, false);
                    }
                }
            }
        });

        btnVoltar.addActionListener(e -> dispose());
        setVisible(true);
    }
}