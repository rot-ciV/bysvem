package bysvem;

import bysvem.modelo.Conta;
import bysvem.modelo.Jogo;
import bysvem.modelo.Gerenciador;
import bysvem.visao.TelaLogin;

import javax.swing.SwingUtilities;
import java.util.ArrayList;

public class Programa {
    public static void main(String[] args) {
        ArrayList<Conta> contas = Gerenciador.carregaContas();
        ArrayList<Jogo> jogos = Gerenciador.carregaJogos();
        Gerenciador.carregarCompras(contas, jogos); 
        Gerenciador.carregarRegistros(jogos, contas);
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}