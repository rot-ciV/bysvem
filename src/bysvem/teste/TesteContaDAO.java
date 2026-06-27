package bysvem.teste;

import bysvem.modelo.Conta;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TesteContaDAO {
    
    private EntidadeDAO<Conta> dao;

    @BeforeEach
    protected void inicializar(){
        dao = new EntidadeDAO<>(Conta.class);
    }

    @AfterEach
    protected void terminou(){
        dao = null;
    }

    @Test
    public void testarSalvar() throws Exception{
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        dao.salvar(conta);
        Conta resultado = dao.carregar(67);
        assertNotNull(resultado);
        assertEquals(67, resultado.getId());
    }

    @Test
    public void testarSalvarIdExistente(){
        Conta primeira = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        Conta segunda = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        dao.salvar(primeira);
        assertThrows(PersistenceException.class, () -> dao.salvar(segunda));
    }

    @Test
    public void testarAtualizarIdInexistente(){
        
        Conta conta = new Usuario(99, "x", 1234, "x", 0.0, false);
        assertThrows(PersistenceException.class, () -> dao.atualizar(conta));
    }

    @Test
    public void testarAtualizarIdExistente(){
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);
        Conta atualizada = new Usuario(67, "Abacate", 1234, "teste@gmail.com", 0.0, false);
        dao.salvar(conta);
        dao.atualizar(atualizada);
        Conta resultado = dao.carregar(67);
        assertEquals("Abacate", resultado.getNome());
    }

    @Test
    void apagarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(999));
    }

    @Test
    void apagarIdExistente() throws Exception {
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        dao.salvar(conta);

        Conta removida = dao.apagar(67);

        assertEquals(67, removida.getId());

        assertThrows(PersistenceException.class, () -> dao.carregar(67));
    }

    @Test
    void carregarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(999));
    }

    @Test
    void carregarIdExistente() throws Exception {
        Conta conta = new Usuario(67, "teste", 1234, "teste@gmail.com", 0.0, false);

        dao.salvar(conta);

        Conta resultado = dao.carregar(67);

        assertNotNull(resultado);
        assertEquals(67, resultado.getId());
        assertEquals("teste", resultado.getNome());
    }

}