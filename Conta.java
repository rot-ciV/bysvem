
public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    private String email;

    public Conta(int id, String nome, int senha, String email){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.email = email;
    }

    public void setNome(String nome){ this.nome = nome;}
    public void setSenha(int senha){ this.senha = senha;}
    public void setEmail(String email){ this.email = email;}
    
    
    public String getNome(){ return this.nome;}
    public int getSenha(){ return this.senha;}
    public String getEmail(){ return this.email;}   
}