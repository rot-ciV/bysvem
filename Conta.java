import java.util.ArrayList; 

public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    protected ArrayList<Jogo> jogosadquiridos;
    private String email;

    public Conta(int id, String nome, int senha, String email){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.jogosadquiridos = new ArrayList<>();
        this.email = email;
    }

    public void addJogo(Jogo novo_game){
        this.jogosadquiridos.add(novo_game);
    }

    public void setNome(String nome){ this.nome = nome;}
    public void setSenha(int senha){ this.senha = senha;}
    public void setEmail(String nome){ this.nome = nome;}

    public String getNome(){ return this.nome;}
    public int getSenha(){ return this.senha;}
    public String getEmail(){ return this.email;}
    public boolean PossuiJogo(Jogo jogo){
        if(jogosadquiridos.contains(jogo))return true;
        return false;
    }      
}