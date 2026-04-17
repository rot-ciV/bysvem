public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    private double saldo;
    // private String dataCriaçao;
    private String[] jogosAdquiridos;

    public Conta(int id, String nome, int senha){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.jogosAdquiridos[0] = "Nenhum";
    }       
}