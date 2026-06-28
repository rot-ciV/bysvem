package bysvem;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import bysvem.modelo.Compra;
import bysvem.modelo.Conta;
import bysvem.modelo.Jogo;
import bysvem.modelo.Registro;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;
import bysvem.visao.TelaLogin;

public class Programa {
    public static void main(String[] args) {
        GerenciadorPersistencia gerenciador = GerenciadorPersistencia.getInstancia();

        try {
            gerenciador.getDAO(Conta.class).recuperar("dados/contas.dat");
        } catch (PersistenceException e) {
            System.out.println("Nenhuma conta encontrada.");
        }
        try {
            gerenciador.getDAO(Jogo.class).recuperar("dados/jogos.dat");
        } catch (PersistenceException e) {
            System.out.println("Nenhum jogo encontrado.");
        }
        try {
            gerenciador.getDAO(Compra.class).recuperar("dados/compras.dat");
        } catch (PersistenceException e) {
            System.out.println("Nenhuma compra encontrada.");
        }
        try {
            gerenciador.getDAO(Registro.class).recuperar("dados/registros.dat");
        } catch (PersistenceException e) {
            System.out.println("Nenhum registro encontrado.");
        }

        // Reconstruir associações de Registro
        try {
            Map<Integer, Conta> mapaContas = new HashMap<>();
            for (Conta c : gerenciador.getDAO(Conta.class).carregarTodos()) {
                mapaContas.put(c.getId(), c);
            }
            Map<Integer, Jogo> mapaJogos = new HashMap<>();
            for (Jogo j : gerenciador.getDAO(Jogo.class).carregarTodos()) {
                mapaJogos.put(j.getId(), j);
            }
            for (Registro reg : gerenciador.getDAO(Registro.class).carregarTodos()) {
                reg.setConta(mapaContas.get(reg.getIdConta()));
                reg.setJogo(mapaJogos.get(reg.getIdJogo()));
            }
        } catch (PersistenceException e) {
            // sem registros
        }

        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}