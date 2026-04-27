
import java.util.ArrayList;

public abstract class Entidade<T>{

    protected int id;
    protected boolean foiSalvo;

    public Entidade() {
        this.id = 0;
        this.foiSalvo = false;
    }

    public Entidade(int id) {
        this.id = id;
        this.foiSalvo = false;
    }

    public void setId(int id){ this.id = id; }
    public void setFoiSalvo(boolean foiSalvo) { this.foiSalvo = foiSalvo; }

    public int getId() { return this.id; }
    public boolean getFoiSalvo() { return  this.foiSalvo; } 

    public String toString(){
        return String.format("\nId: %d, Foi Salvo: %b", id, foiSalvo);
    }

    public boolean salvar(ArrayList<T> lista) {
        if (foiSalvo) return false;
        foiSalvo = true;
        return true;
    }

    public boolean atualizar(ArrayList<T> lista) {
        if (!foiSalvo) return false;
        return true;
    }

    public boolean apagar(int id, ArrayList<T> lista) {
        if (!foiSalvo) return false;
        foiSalvo = false;
        return true;    
    }

    public boolean carregar(int id, ArrayList<T> lista) {
        this.id = id;
        foiSalvo = true;
        return true;
    }

    public abstract ArrayList<T> carregarTodos();  
}