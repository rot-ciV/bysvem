package bysvem.teste;

import bysvem.modelo.*;
import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TesteCompraDAO {

    private EntidadeDAO<Compra> dao;

    @BeforeEach
    protected void inicializar() {
        dao = new EntidadeDAO<>(Compra.class);
    }

    @AfterEach
    protected void terminou() {
        dao = null;
    }

    private Usuario criarUsuario() {
        return new Usuario(1, "teste", 1234, "teste@gmail.com", 0.0, false);
    }

    private Jogo criarJogo() {
        return new Jogo(12345, "teste", "teste", "teste", 50.0, "teste");
    }

    private List<ItemCompra> criarItens() {
        List<ItemCompra> itens = new ArrayList<>();
        itens.add(new ItemCompra(1, criarJogo(), 50.00));
        return itens;
    }

    @Test
    void testarSalvar() throws Exception {
        Compra compra = new Compra(
                7,
                criarUsuario(),
                LocalDate.now(),
                criarItens()
        );

        dao.salvar(compra);

        Compra resultado = dao.carregar(7);

        assertNotNull(resultado);
        assertEquals(7, resultado.getId());
        assertEquals(50.00, resultado.getValorTotal());
    }

    @Test
    void testarSalvarIdExistente() throws Exception {
        Compra c1 = new Compra(10, criarUsuario(), LocalDate.now(), criarItens());
        Compra c2 = new Compra(10, criarUsuario(), LocalDate.now(), criarItens());

        dao.salvar(c1);

        assertThrows(PersistenceException.class, () -> dao.salvar(c2));
    }

    @Test
    void testarAtualizarIdInexistente() {
        Compra compra = new Compra(99, criarUsuario(), LocalDate.now(), criarItens());

        assertThrows(PersistenceException.class, () -> dao.atualizar(compra));
    }

    @Test
    void testarAtualizarIdExistente() throws Exception {
        Compra original = new Compra(20, criarUsuario(), LocalDate.now(), criarItens());
        dao.salvar(original);

        List<ItemCompra> novosItens = new ArrayList<>();
        novosItens.add(new ItemCompra(2, criarJogo(), 100.00));

        Compra atualizada = new Compra(20, criarUsuario(), LocalDate.now(), novosItens);

        dao.atualizar(atualizada);

        Compra resultado = dao.carregar(20);

        assertEquals(100.00, resultado.getValorTotal());
    }

    @Test
    void apagarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.apagar(999));
    }

    @Test
    void apagarIdExistente() throws Exception {
        Compra compra = new Compra(30, criarUsuario(), LocalDate.now(), criarItens());

        dao.salvar(compra);

        Compra removida = dao.apagar(30);

        assertEquals(30, removida.getId());

        assertThrows(PersistenceException.class, () -> dao.carregar(30));
    }

    @Test
    void carregarIdInexistente() {
        assertThrows(PersistenceException.class, () -> dao.carregar(999));
    }

    @Test
    void carregarIdExistente() throws Exception {
        Compra compra = new Compra(40, criarUsuario(), LocalDate.now(), criarItens());

        dao.salvar(compra);

        Compra resultado = dao.carregar(40);

        assertNotNull(resultado);
        assertEquals(40, resultado.getId());
        assertEquals("teste", resultado.getUsuario().getNome());
    }
}