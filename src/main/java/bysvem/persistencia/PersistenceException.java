package bysvem.persistencia;

import bysvem.modelo.Entidade;

public class PersistenceException extends Exception{


    public PersistenceException(String operacao, String problema, int id) {
        super("Não foi possível " + operacao + " o conteúdo de id: " + id + ". Razão: " + problema);
    }
    
    public PersistenceException(String operacao, String problema, Entidade entidade){
        super("Não foi possível " + operacao + " o objeto: " + entidade + ". Razão: " + problema);
    }
    
}
