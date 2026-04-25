public class Operador extends Conta{
    
    
    public Operador(int id, String nome, int senha, String email){
        super(id,nome,senha,email);
    }

    @Override
    public String toString(){
        return String.format("\n%s", super.toString());
    }
}