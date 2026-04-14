public abstract class Bysvem{

    protected long id;
    protected boolean sacramento; // isso aqui é quando é persistido (jackson)

    public Bysvem() {
        
        this.id = 0;
    }

    public Bysvem(long id) {
        this.id = id;
    }



}