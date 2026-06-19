package bysvem.visao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Carrega todos os jogos e registros
        ArrayList<Jogo> todosJogos = Gerenciador.carregaJogos();
        ArrayList<Conta> todasContas = Gerenciador.carregaContas();
        ArrayList<Registro> registros = Gerenciador.CarregaRegistros(todosJogos, todasContas);

        // Filtra os jogos que o usuário possui (com base nos registros)
        jogosDoUsuario = new ArrayList<>();
        for (Registro reg : registros) {
            if (reg.getConta().getId() == usuarioLogado.getId()) {
                // Verifica se o jogo existe na lista de todos os jogos
                Jogo jogo = reg.getJogo();
                if (jogo != null) {
                    jogosDoUsuario.add(jogo);
                }
            }
        }

        // Se não houver jogos, exibe mensagem
        if (jogosDoUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Sua biblioteca está vazia.\nAdquira jogos na loja!",
                    "Biblioteca Vazia", JOptionPane.INFORMATION_MESSAGE);
        }

        // Painel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título com nome do usuário
        JLabel titulo = new JLabel("Biblioteca de " + usuarioLogado.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        // Lista de nomes dos jogos do usuário
        modeloLista = new DefaultListModel<>();
        for (Jogo j : jogosDoUsuario) {
            modeloLista.addElement(j.getNome());
        }
        listaNomes = new JList<>(modeloLista);
        listaNomes.setFont(new Font("Arial", Font.PLAIN, 14));
        listaNomes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(listaNomes);
        panel.add(scroll, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnDetalhes = new JButton("Ver detalhes");
        JButton btnVoltar = new JButton("Voltar");

        btnDetalhes.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));

        panelBotoes.add(btnDetalhes);
        panelBotoes.add(btnVoltar);
        panel.add(panelBotoes, BorderLayout.SOUTH);

        add(panel);

        // Ações
        btnDetalhes.addActionListener(e -> mostrarDetalhes());
        listaNomes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mostrarDetalhes();
                }
            }
        });
        btnVoltar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void mostrarDetalhes() {
        int idx = listaNomes.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um jogo na lista.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jogo jogoSelecionado = jogosDoUsuario.get(idx);

        // Diálogo de detalhes (similar ao de Jogos_Disponiveis, mas sem botão "Adquirir")
        JDialog dialog = new JDialog(this, "Detalhes do Jogo", true);
        dialog.setSize(500, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.LINE_START;

        Font fonteDetalhes = new Font("Arial", Font.PLAIN, 14);
        Font fonteTitulo = new Font("Arial", Font.BOLD, 16);

        // Linha 1: Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        JLabel lblNome = new JLabel(jogoSelecionado.getNome());
        lblNome.setFont(fonteTitulo);
        panel.add(lblNome, gbc);

        // Linha 2: Gênero
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Gênero:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(jogoSelecionado.getGenero()), gbc);

        // Linha 3: Desenvolvedora
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Desenvolvedora:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(jogoSelecionado.getDesenvolvedora()), gbc);

        // Linha 4: Preço
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Preço:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("R$ " + String.format("%.2f", jogoSelecionado.getPreco())), gbc);

        // Linha 5: Descrição (ocupa as duas colunas)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Descrição:"), gbc);

        gbc.gridy = 6;
        JTextArea descArea = new JTextArea(jogoSelecionado.getDesc());
        descArea.setFont(fonteDetalhes);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setEditable(false);
        descArea.setBackground(panel.getBackground());
        JScrollPane scrollDesc = new JScrollPane(descArea);
        scrollDesc.setPreferredSize(new Dimension(400, 80));
        panel.add(scrollDesc, gbc);

        // Linha 7: Botão "Voltar" (não tem "Adquirir" na biblioteca)
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnVoltarDetalhes = new JButton("Voltar");
        btnVoltarDetalhes.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltarDetalhes.addActionListener(e -> dialog.dispose());

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.add(btnVoltarDetalhes);
        panel.add(painelBotoes, gbc);

        dialog.add(panel);
        dialog.setVisible(true);
    }
}