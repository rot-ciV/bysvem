public class Usuario extends Conta{
    
    private double saldo;

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban){
        
        super(id,nome,senha, email, ban);
        this.saldo = saldo;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return this.saldo; }

    @Override
    public String toString(){
        return String.format("\n%s, Saldo: %ff", super.toString(), saldo);
    }

}