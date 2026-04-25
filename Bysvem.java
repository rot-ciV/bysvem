public abstract class Bysvem{

    protected int id;
    protected boolean foiSalvo;

    public Bysvem() {
        this.id = 0;
    }

    public Bysvem(int id) {
        this.id = id;
    }

    public void salvar(){

        this.foiSalvo = true;
    }

    public void setId(int id){ this.id = id; }
    public void setFoisalvo(boolean foiSalvo) { this.foiSalvo = foiSalvo; }

    public int getId() { return this.id; }
    public boolean getFoiSalvo() { return  this.foiSalvo; } 

    public String toString(){
        return String.format("\nId: %d, Foi Salvo: %b", id, foiSalvo);
    }
}