public abstract class Conta extends Bysvem{

    private String nome;
    private int senha;
    private double saldo;
    // private String dataCriaçao;
    private String[] jogosAdquiridos;
    private double horasJogadas;

    public Conta(int id, String nome, int senha){
        super();
        this.nome = nome;
        this.senha = senha;
        this.jogosAdquiridos[0] = Nenhum;
        this.horasJogadas = 0.0
    }       
}