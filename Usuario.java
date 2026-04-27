import java.util.ArrayList;

public class Usuario extends Conta{
    
    private double saldo;

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban){
        
        super(id,nome,senha, email, ban);
        this.saldo = saldo;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return this.saldo; }

    /*ESSE MÉTODO ERA DE LOJA, PASSEI PARA CÁ E EM LOJA ESTÁ COMENTADO */
    public boolean compraJogo(Jogo jogoComprado, ArrayList<Conta> contas, ArrayList<Registro> registros, Loja loja){

        if (getSaldo() >= jogoComprado.getPreco()){

            
            double novoSaldo = getSaldo() - jogoComprado.getPreco();
            for(int i = 0; i < contas.size(); i++){
                if(contas.get(i).getId() == getId()){
                    setSaldo(novoSaldo); 
                    ((Usuario)contas.get(i)).setSaldo(novoSaldo);
                }
            }
            //MUDEI
            if(atualizar(contas)){
                int id = loja.criaId(1);
                Registro novoRegistro = new Registro(id, jogoComprado, this, 0.0);

                return novoRegistro.salvar(registros);
            }
        }

        return false;
    }

    /*MÉTODO ORIGINALMENTE NA LOJA, FAZ MAIS SENTIDO AQUI*/
    public ArrayList<Jogo> biblioteca(ArrayList<Registro> registros){

        ArrayList<Jogo> biblioteca = new ArrayList<>();

        for(int i = 0; i < registros.size(); i++){

            if (getId() == registros.get(i).getConta().getId()){

                biblioteca.add(registros.get(i).getJogo());
            }
        }

        return biblioteca;
    }

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