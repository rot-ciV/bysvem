package bysvem.persistencia;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorPersistencia{

    private static GerenciadorPersistencia instancia;
    private Map<String, EntidadeDAO> listaEntidade;

    private GerenciadorPersistencia(){
        listaEntidade = new HashMap<>();
    }

    public static GerenciadorPersistencia getInstancia() { 
        
        if(instancia == null){

            instancia = new GerenciadorPersistencia();
            return instancia;
        }

        return instancia; 
    }

    public EntidadeDAO getDAO(String entidade){

        if(!listaEntidade.containsKey(entidade)){
            
            EntidadeDAO novoDAO =  new EntidadeDAO();
            listaEntidade.put(entidade, novoDAO);
        }

        return listaEntidade.get(entidade); 
    }
}