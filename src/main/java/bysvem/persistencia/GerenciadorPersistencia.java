package bysvem.persistencia;

import java.util.HashMap;
import java.util.Map;

import bysvem.modelo.Entidade;

public class GerenciadorPersistencia {

    private static GerenciadorPersistencia instancia;
    private Map<Class<?>, EntidadeDAO<?>> listaEntidade = new HashMap<>();

    private GerenciadorPersistencia() {}

    public static GerenciadorPersistencia getInstancia() {
        if (instancia == null) {
            instancia = new GerenciadorPersistencia();
        }
        return instancia;
    }

    public <E extends Entidade> EntidadeDAO<E> getDAO(Class<E> classe) {
        if (!listaEntidade.containsKey(classe)) {
            // Passa a classe para o construtor
            listaEntidade.put(classe, new EntidadeDAO<>(classe));
        }
        return (EntidadeDAO<E>) listaEntidade.get(classe);
    }
}