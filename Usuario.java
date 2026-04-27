import java.util.ArrayList;

public class Usuario extends Conta{
    
    private double saldo;

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban){
        
        super(id,nome,senha, email, ban);
        this.saldo = saldo;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return this.saldo; }

    @Override
    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                super.carregar(id, listaContas);
                Usuario carregado = (Usuario) listaContas.get(i);
                this.saldo = carregado.getSaldo();

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        return String.format("\n%s, Saldo: %ff", super.toString(), saldo);
    }

}