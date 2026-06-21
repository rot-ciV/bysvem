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

import bysvem.modelo.Gerenciador;
import bysvem.modelo.Jogo;
import bysvem.modelo.Conta;
import bysvem.modelo.Usuario;

public class Jogos_Disponiveis extends JFrame {

    private ArrayList<Jogo> jogos;
    private JList<String> listaNomes;
    private DefaultListModel<String> modeloLista;
    private Usuario usuarioLogado; // novo

    public Jogos_Disponiveis(Usuario usuario) {
        this.usuarioLogado = usuario;
        setTitle("Jogos Disponíveis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        jogos = Gerenciador.carregaJogos();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Catálogo de Jogos", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        for (Jogo j : jogos) {
            modeloLista.addElement(j.getNome());
        }
        listaNomes = new JList<>(modeloLista);
        listaNomes.setFont(new Font("Arial", Font.PLAIN, 28));
        listaNomes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(listaNomes);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnDetalhes = new JButton("Ver detalhes");
        JButton btnVoltar = new JButton("Voltar");

        btnDetalhes.setFont(new Font("Arial", Font.BOLD, 20));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 20));

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
            Jogo jogoSelecionado = jogos.get(idx);
            Detalhes_Usuario.exibirDetalhes(this, jogoSelecionado, true, usuarioLogado);
        });

        listaNomes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int idx = listaNomes.getSelectedIndex();
                    if (idx != -1) {
                        Jogo jogoSelecionado = jogos.get(idx);
                        // Correção: usar NomeDaClasse.this
                        Detalhes_Usuario.exibirDetalhes(Jogos_Disponiveis.this, jogoSelecionado, true, usuarioLogado);
                    }
                }
            }   
        });
        
        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }
}