package bysvem.modelo;

public abstract class Conta extends Entidade{

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

    @Override
    public String toString(){
        String nome =("Nome do perfil: " + getNome());
        String senha = ("Senha: " + getSenha());
        return String.format("\n%s\n%s", nome, senha);
    }
}