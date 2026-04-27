
public class Operador extends Conta{
    
    
    public Operador(int id, String nome, int senha, String email, boolean ban){
        super(id,nome,senha,email, ban);
    }

    public void banConta(Conta conta){
        conta.setBan(true);
    }

    @Override
    public String toString(){
        return String.format("\n%s", super.toString());
    }
}