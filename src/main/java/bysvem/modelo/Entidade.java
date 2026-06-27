package bysvem.modelo;

import java.io.Serializable;

public abstract class Entidade implements Comparable<Entidade>, Serializable{

    protected int id;

    public Entidade() {
        this.id = 0;
    }

    public Entidade(int id) {
        this.id = id;
    }

    public void setId(int id){ this.id = id; }

    public int getId() { return this.id; }

    public String toString(){
        return String.format("\nId: %d", id);
    }

    @Override
    public boolean equals(Object obj){

        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false; 
        Entidade entidade = (Entidade) obj;

        return this.id == entidade.id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }

    @Override
    public int compareTo(Entidade entidade){

        return Integer.compare(id, entidade.id);
    }
}