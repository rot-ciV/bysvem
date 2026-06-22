package bysvem.testes;

import bysvem.modelo.Jogo;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

public class TesteJogoDAO {

    public static void main(String[] args) {
        
        System.out.println("---- TESTE JOGO DAO ----");
        testarSalvar();
        testarSalvarComErro();
        testarAtualizar();
        testarAtualizarSemSalvar();
        testeApagar();
        testeApagarIdInexistente();
        testeCarregar();
        testeCarregarIdInexistente();
    }


    public static void testarSalvar(){

        System.out.println("Teste do método SALVAR:");

        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo novoJogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");

        try {
            dao.salvar(novoJogo);
            Jogo jogoSalvo = dao.carregar(676767);

            if(jogoSalvo != null && jogoSalvo.getId() == 676767){
                System.out.println("Está funionando corretamente.");
            }

        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testarSalvarComErro(){

        System.out.println("Teste de salvamento com id's iguais:");

        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo jogo = new Jogo(1, "teste", "teste", "teste", 6.7, "jogo teste");
        Jogo copiaJogo = new Jogo(1, "teste", "teste", "teste", 6.7, "jogo teste");

        try {
            dao.salvar(jogo);
        } catch (PersistenceException e) {
            System.out.println("Erro no primeiro salvamento. Teste inconclusivo");
        }

        try {
            dao.salvar(copiaJogo);
            System.out.println("ERRO! Dois jogos com o mesmo id foram salvos!");
        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }

    }

    public static void testarAtualizar(){

        System.out.println("Teste do método ATUALIZAR:");
        
        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");
        Jogo jogoAtualizado = new Jogo(676767, "atualizado", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");

        try {

            dao.salvar(jogo);
            dao.atualizar(jogoAtualizado);
            Jogo teste = dao.carregar(676767);

            if(teste.getNome().equalsIgnoreCase("atualizado")){
                System.out.println("Está funcionando corretamente.");
            }

        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testarAtualizarSemSalvar(){

        System.out.println("Teste se um jogo não salvo pode ser atualizado:");

        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo jogoNaoSalvo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");

        try {
            dao.atualizar(jogoNaoSalvo);
            System.out.println("ERRO! Jogo não salvo está sendo atualizado");

        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }
    }

    public static void testeApagar(){

        System.out.println("Teste do método APAGAR:");

        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo jogoNoLixo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");

        try {
            dao.salvar(jogoNoLixo);
            Jogo jogoApagado = dao.apagar(676767);

            if(jogoApagado.getId() == 676767){
                System.out.println("Está funcionando corretamente.");
            }else{
                System.out.println("O método apagar não está apagando o objeto com o id especificado");
            }
            
        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testeApagarIdInexistente(){

        System.out.println("Teste se é possível apagar um id inexistente:");
        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();

        try {
            dao.apagar(1);
            System.out.println("O método apagou um id inexistente");

        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        } 
    }

    public static void testeCarregar(){

        System.out.println("Teste do método CARREGAR");

        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.00, "jogo criado apenas para testar");

        try {
            dao.salvar(jogo);
            Jogo jogoCarregado = dao.carregar(676767);

            if(jogo.getId() == jogoCarregado.getId()){
                System.out.println("Está funcionando corretamente.");
            }else{
                System.out.println("O método está carregando um jogo diferente ao id solicitado.");
            }
        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testeCarregarIdInexistente(){

        System.out.println("Teste se é possível carregar um objeto com id inexistente:");
        
        EntidadeDAO<Jogo> dao = new EntidadeDAO<>();

        try {
            Jogo naoDeveriaExistir = dao.carregar(676767);
            System.out.println("O método está carregando um jogo que não existe.");
            
        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }
    }
}