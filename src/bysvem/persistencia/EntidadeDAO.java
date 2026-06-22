package bysvem.persistencia;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

import bysvem.modelo.Entidade;

public class EntidadeDAO <E extends Entidade>{
    
    protected Set<E> entidadesSalvas;

    public EntidadeDAO() {
        this.entidadesSalvas = new HashSet<>();
    }

    public void salvar(E entidade) throws PersistenceException{
        
        if(!entidadesSalvas.add(entidade)) throw new PersistenceException("salvar", "Já foi salvo", entidade);
    }

    public void atualizar(E entidade) throws PersistenceException{

        for(E entidadeAtual : entidadesSalvas){

            if(entidadeAtual.getId() == entidade.getId()){

                entidadesSalvas.remove(entidadeAtual);
                entidadesSalvas.add(entidade);
                return;
            }
        }

        throw new PersistenceException("atualizar", "Objeto não existe no banco de dados", entidade);
    }

    public E apagar(int id) throws PersistenceException{
        
        for(E entidadeAtual : entidadesSalvas){

            if(entidadeAtual.getId() == id){
                entidadesSalvas.remove(entidadeAtual);
                return entidadeAtual;
            }
        }

        throw new PersistenceException("apagar", "Não existe um objeto com o id indicado no banco de dados.", id);    
    }

    public E carregar(int id) throws PersistenceException{
        
        for(E entidadeAtual : entidadesSalvas){

            if(entidadeAtual.getId() == id){
                return entidadeAtual;
            }
        }

        throw new PersistenceException("carregar", "Não existe um objeto com o id indicado no banco de dados.", id);
    }

    public E[] carregarTodos() throws PersistenceException{

        if(entidadesSalvas.isEmpty()) throw new PersistenceException("carregarTodos", "Não há nenhum objeto salvo no banco de dados", null);

        E[] entidadesCarregadas = (E[]) new Entidade[this.entidadesSalvas.size()];
        Set<E> copia = new HashSet<>(this.entidadesSalvas);
        int controle = 0;

        while(!copia.isEmpty()){

            E menorId = null;

            for(E entidadeAtual : copia){

                if (menorId == null){
                    menorId = entidadeAtual;
                    continue;
                }

                if(menorId.getId() > entidadeAtual.getId()){
                    menorId = entidadeAtual;
                }
            }

            entidadesCarregadas[controle] = menorId;
            copia.remove(menorId);
            controle++;
        }
        
        return entidadesCarregadas;
    }

    public void persistir(String caminho) throws PersistenceException{

        try {
            FileOutputStream arquivo = new FileOutputStream(caminho);
            ObjectOutputStream escritor = new ObjectOutputStream(arquivo);
            escritor.writeObject(this.entidadesSalvas);
            escritor.close();
            arquivo.close();

        } catch (Exception e) {
            throw new PersistenceException("persistir", "Erro ao gravar arquivo: " + e.getMessage(), 0);
        }
        
    }

    public void recuperar(String caminho) throws PersistenceException{

        try{
            FileInputStream arquivo = new FileInputStream(caminho);
            ObjectInputStream leitor = new ObjectInputStream(arquivo);
            this.entidadesSalvas = (Set<E>) leitor.readObject();
            leitor.close();
            arquivo.close();

        }catch(Exception e){
            throw new PersistenceException("recuperar", "Erro ao ler o arquivo: " + e.getMessage(), 0);
        }

    }
}
