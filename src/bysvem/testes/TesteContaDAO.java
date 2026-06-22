package bysvem.testes;

import bysvem.modelo.Conta;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

public class TesteContaDAO {

    public static void main(String[] args) {
        
        System.out.println("---- TESTE CONTA DAO ----");
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

        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);


        try {
            dao.salvar(conta);
            Conta contaSalva = dao.carregar(67);

            if(contaSalva != null && contaSalva.getId() == 67){
                System.out.println("Está funionando corretamente.");
            }

        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testarSalvarComErro(){

        System.out.println("Teste de salvamento com id's iguais:");

        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        Conta copiaConta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        try {
            dao.salvar(conta);
        } catch (PersistenceException e) {
            System.out.println("Erro no primeiro salvamento. Teste inconclusivo");
        }

        try {
            dao.salvar(copiaConta);
            System.out.println("ERRO! Duas contas com o mesmo id foram salvos!");
        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }

    }

    public static void testarAtualizar(){

        System.out.println("Teste do método ATUALIZAR:");
        
        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        Conta contaAtualizada = new Usuario(67, "Xx_teste_Gamer_xX", 1234, "teste@gmail.com", 0.0, false);
        try {

            dao.salvar(conta);
            dao.atualizar(contaAtualizada);
            Conta teste = dao.carregar(67);

            if(teste.getNome().equalsIgnoreCase("Xx_teste_Gamer_xX")){
                System.out.println("Está funcionando corretamente.");
            }

        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testarAtualizarSemSalvar(){

        System.out.println("Teste se uma conta não salva pode ser atualizada:");

        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
       Conta contaNaoSalva = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        try {
            dao.atualizar(contaNaoSalva);
            System.out.println("ERRO! Conta não salva está sendo atualizada");

        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }
    }

    public static void testeApagar(){

        System.out.println("Teste do método APAGAR:");

        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
        Conta contaProLixo = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        try {
            dao.salvar(contaProLixo);
            Conta contaApagada = dao.apagar(67);

            if(contaApagada.getId() == 67){
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
        EntidadeDAO<Conta> dao = new EntidadeDAO<>();

        try {
            dao.apagar(1);
            System.out.println("O método apagou um id inexistente");

        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        } 
    }

    public static void testeCarregar(){

        System.out.println("Teste do método CARREGAR");

        EntidadeDAO<Conta> dao = new EntidadeDAO<>();
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        try {
            dao.salvar(conta);
            Conta contaCarregada = dao.carregar(67);

            if(conta.getId() == contaCarregada.getId()){
                System.out.println("Está funcionando corretamente.");
            }else{
                System.out.println("O método está carregando uma conta diferente ao id solicitado.");
            }
        } catch (PersistenceException e) {
            System.out.println("ERRO! " + e.getMessage());
        }
    }

    public static void testeCarregarIdInexistente(){

        System.out.println("Teste se é possível carregar um objeto com id inexistente:");
        
        EntidadeDAO<Conta> dao = new EntidadeDAO<>();

        try {
            Conta naoDeveriaExistir = dao.carregar(67);
            System.out.println("O método está carregando uma conta que não existe.");
            
        } catch (PersistenceException e) {
            System.out.println("Está funcionando corretamente.");
        }
    }
}
