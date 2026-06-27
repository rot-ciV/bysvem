package bysvem;

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
        // Carregar todos os DAOs do arquivo (se existirem)
        GerenciadorPersistencia gerenciador = GerenciadorPersistencia.getInstancia();
        try {
            gerenciador.getDAO(Conta.class).recuperar("dados/contas.dat");
            gerenciador.getDAO(Jogo.class).recuperar("dados/jogos.dat");
            gerenciador.getDAO(Compra.class).recuperar("dados/compras.dat");
            gerenciador.getDAO(Registro.class).recuperar("dados/registros.dat");
        } catch (PersistenceException e) {
            // Arquivos ainda não existem (primeira execução) – ignorar
            System.out.println("Nenhum dado persistido encontrado. Iniciando com conjuntos vazios.");
        }

        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}