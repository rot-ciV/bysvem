public class Usuario extends Conta{
    
    private double saldo;

    public Usuario(int id, String nome, int senha, String email){
        
        super(id,nome,senha, email);
        this.saldo = 0.0;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return this.saldo; }

}