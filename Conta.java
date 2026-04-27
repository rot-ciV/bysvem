import java.util.ArrayList;

public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    private String email;
    private boolean ban;

    public Conta(int id, String nome, int senha, String email, boolean ban){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.ban = ban;
    }

    public void setNome(String nome){ this.nome = nome;}
    public void setSenha(int senha){ this.senha = senha;}
    public void setEmail(String email){ this.email = email;}
    public void setBan(boolean ban) { this.ban = ban; }
    
    
    public String getNome(){ return this.nome;}
    public int getSenha(){ return this.senha;}
    public String getEmail(){ return this.email;}
    public boolean getBan() { return this.ban; }

    public boolean salvar(ArrayList<Conta> listaContas){

        if(this.foiSalvo){
            return false;
        }

        listaContas.add(this);
        this.setFoiSalvo(true);
        Gerenciador.salvarContas(listaContas);
        return true;
    }

    public boolean atualizar(ArrayList<Conta> listaContas){

        if(!this.foiSalvo){
            return false;
        }

        Gerenciador.salvarContas(listaContas);
        return true;
    }

    public boolean apagar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                Conta apagada = listaContas.get(i);
                apagada.setFoiSalvo(false);
                listaContas.remove(i);
                Gerenciador.salvarContas(listaContas);
                return true;
            }
        }

        return false;
    }

    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                this.id = listaContas.get(i).getId();
                this.nome = listaContas.get(i).getNome();
                this.senha = listaContas.get(i).getSenha();
                this.email = listaContas.get(i).getEmail();
                this.ban = listaContas.get(i).getBan();
                this.setFoiSalvo(true);

                return true;
            }
        }

        return false;
    }

    public ArrayList<Conta> carregarTodos() {
        return Gerenciador.carregaContas();
    }

    @Override
    public String toString(){
        return String.format("\n%s, Nome: %s, Senha: %d, Email: %s, Ban: %b", super.toString(), nome, senha, email, ban);
    }
}