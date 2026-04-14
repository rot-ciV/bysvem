public abstract class Bysvem{

    protected int id;
    protected boolean foiSalvo;

    public Bysvem() {
        this.id = 0;
    }

    public Bysvem(int id) {
        this.id = id;
    }

    public abstract void salvar(); // Faz sentido?

    public void setId(int id){ this.id = id; }
    public int getId() { return this.id; }
}