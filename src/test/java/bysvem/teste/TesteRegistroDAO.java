package bysvem.teste;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bysvem.modelo.Jogo;
import bysvem.modelo.Registro;
import bysvem.modelo.Usuario;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

class TesteRegistroDAO {

    private EntidadeDAO<Registro> dao;

    @BeforeEach
    protected void inicializar() {
        dao = new EntidadeDAO<>(Registro.class);
    }

    @AfterEach
    protected void terminou() {
        dao = null;
    }

    private Usuario criarUsuario() {
        return new Usuario(1, "Abacate", 1234, "abacate@gmail.com", 0.0, false);
    }

    private Jogo criarJogo() {
        return new Jogo(12345, "teste", "teste", "teste", 50.0, "teste");
    }

    @Test
    void testarSalvar() throws Exception {
        Registro registro = new Registro(
                1,
                criarJogo(),
                criarUsuario(),
                15.5
        );

        dao.salvar(registro);

        Registro resultado = dao.carregar(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals(15.5, resultado.getHorasJogadas());
    }

    @Test
    void testarSalvarIdExistente() throws Exception {
        Registro r1 = new Registro(10, criarJogo(), criarUsuario(), 10.0);
        Registro r2 = new Registro(10, criarJogo(), criarUsuario(), 20.0);

        dao.salvar(r1);

        assertThrows(PersistenceException.class, () -> dao.salvar(r2));
    }

    @Test
    void testarAtualizarIdInexistente() {
        Registro registro = new Registro(99, criarJogo(), criarUsuario(), 50.0);

        assertThrows(PersistenceException.class, () -> dao.atualizar(registro));
    }

    @Test
    void testarAtualizarIdExistente() throws Exception {
        Registro original = new Registro(20, criarJogo(), criarUsuario(), 10.0);

        dao.salvar(original);

        Registro atualizado = new Registro(20, criarJogo(), criarUsuario(), 100.0);

        dao.atualizar(atualizado);

        Registro resultado = dao.carregar(20);

        assertEquals(100.0, resultado.getHorasJogadas());
    }

    @Test
    void apagarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(999));
    }

    @Test
    void apagarIdExistente() throws Exception {
        Registro registro = new Registro(30, criarJogo(), criarUsuario(), 20.0);

        dao.salvar(registro);

        Registro removido = dao.apagar(30);

        assertEquals(30, removido.getId());

        assertThrows(PersistenceException.class, () -> dao.carregar(30));
    }

    @Test
    void carregarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(999));
    }

    @Test
    void carregarIdExistente() throws Exception {
        Registro registro = new Registro(40, criarJogo(), criarUsuario(), 12.5);

        dao.salvar(registro);

        Registro resultado = dao.carregar(40);

        assertNotNull(resultado);
        assertEquals(40, resultado.getId());
        assertEquals(12.5, resultado.getHorasJogadas());
        assertEquals("teste", resultado.getJogo().getNome());
    }
}