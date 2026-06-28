package bysvem.modelo;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.PersistenceException;

public class IdUtil {

    public static int gerarIdConta() {
        return gerarIdConta(null);
    }
    public static int gerarIdConta(EntidadeDAO<Conta> dao) {
        int id;
        do {
            id = (int) (Math.random() * 900) + 100;
        } while (dao != null && idExiste(dao, id));
        return id;
    }

    public static int gerarIdJogo() {
        return gerarIdJogo(null);
    }
    public static int gerarIdJogo(EntidadeDAO<Jogo> dao) {
        int id;
        do {
            id = (int) (Math.random() * 9000) + 1000;
        } while (dao != null && idExiste(dao, id));
        return id;
    }

    public static int gerarIdCompra() {
        return gerarIdCompra(null);
    }
    public static int gerarIdCompra(EntidadeDAO<Compra> dao) {
        int id;
        do {
            id = (int) (Math.random() * 90000) + 10000;
        } while (dao != null && idExiste(dao, id));
        return id;
    }

    private static boolean idExiste(EntidadeDAO<? extends Entidade> dao, int id) {
        try {
            dao.carregar(id);
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }
}