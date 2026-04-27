
public abstract class Entidade{

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
}