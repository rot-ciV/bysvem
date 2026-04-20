import java.util.ArrayList; 

public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    protected ArrayList<Jogo> jogosadquiridos;

    public Conta(int id, String nome, int senha){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.jogosadquiridos = new ArrayList<>();
    }

    public void addJogo(Jogo novo_game){
        this.jogosadquiridos.add(novo_game);
    }

    public void setNome(String nome){ this.nome = nome;}
    public void setSenha(int senha){ this.senha = senha;}

    public String getNome(){ return this.nome;}
    public int getSenha(){ return this.senha;}
    public boolean PossuiJogo(Jogo jogo){
        if(jogosadquiridos.contains(jogo))return true;
        return false;
    }      
}