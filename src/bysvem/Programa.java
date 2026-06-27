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
        } catch (PersistenceException e) {
            // Arquivos ainda não existem (primeira execução) – ignorar
            System.out.println("Nenhuma conta encontrada. Iniciando com conjunto vazio.");
        }
        try {
            gerenciador.getDAO(Jogo.class).recuperar("dados/jogos.dat");
        } catch (PersistenceException e) {
            // Arquivos ainda não existem (primeira execução) – ignorar
            System.out.println("Nenhum jogo encontrado. Iniciando com conjunto vazio.");
        }
        try {
            gerenciador.getDAO(Compra.class).recuperar("dados/compras.dat");
        } catch (PersistenceException e) {
            // Arquivos ainda não existem (primeira execução) – ignorar
            System.out.println("Nenhuma compra encontrada. Iniciando com conjunto vazio.");
        }
        try {
            gerenciador.getDAO(Registro.class).recuperar("dados/registros.dat");
        } catch (PersistenceException e) {
            // Arquivos ainda não existem (primeira execução) – ignorar
            System.out.println("Nenhuma registro encontrado. Iniciando com conjunto vazio.");
        }
        
        SwingUtilities.invokeLater(() -> new TelaLogin());
    }
}