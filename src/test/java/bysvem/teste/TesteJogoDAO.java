package bysvem.teste;

import bysvem.modelo.Jogo;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TesteJogoDAO {

    private EntidadeDAO<Jogo> dao;

    @BeforeEach
    protected void Inicializar() throws Exception{
        dao = new EntidadeDAO<>(Jogo.class);
    }

    @AfterEach
    protected void terminou() throws Exception{
        dao = null;
    }

    @Test
    void testarSalvar() throws Exception {
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        dao.salvar(jogo);

        Jogo resultado = dao.carregar(676767);

        assertNotNull(resultado);
        assertEquals(676767, resultado.getId());
    }

    @Test
    public void testarSalvarIdExistente() throws Exception {
        Jogo original = new Jogo(1, "teste", "teste", "teste", 6.7, "jogo teste");
        Jogo copia = new Jogo(1, "teste", "teste", "teste", 6.7, "jogo teste");

        dao.salvar(original);

        assertThrows(PersistenceException.class, () -> dao.salvar(copia));
    }

    @Test
    public void testarAtualizarIdInexistente() throws Exception{
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        assertThrows(PersistenceException.class, () -> dao.atualizar(jogo));
    }

    @Test
    public void testarAtualizarIdExistente() throws Exception{
        Jogo original = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        Jogo atualizado = new Jogo(676767, "atualizado", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        dao.salvar(original);
        dao.atualizar(atualizado);

        Jogo resultado = dao.carregar(676767);

        assertEquals("atualizado", resultado.getNome());
    }

    @Test
    public void apagarIdInexistente() throws Exception{
        assertThrows(PersistenceException.class, () -> dao.apagar(999));
    }

    @Test
    public void apagarIdExistente() throws Exception {
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        dao.salvar(jogo);

        Jogo removido = dao.apagar(676767);

        assertEquals(676767, removido.getId());

        assertThrows(PersistenceException.class, () -> dao.carregar(676767));
    }


    @Test
    public void carregarIdInexistente() throws Exception{
        assertThrows(PersistenceException.class, () -> dao.carregar(676767));
    }

    @Test
    public void carregarIdExistente() throws Exception {
        Jogo jogo = new Jogo(676767, "teste", "RPG", "bysvem comporations", 10.0,
                "jogo criado apenas para testar");

        dao.salvar(jogo);

        Jogo resultado = dao.carregar(676767);

        assertNotNull(resultado);
        assertEquals(676767, resultado.getId());
        assertEquals("teste", resultado.getNome());
    }
}