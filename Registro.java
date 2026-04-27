
import java.util.ArrayList;

public class Registro extends Bysvem{

    private Jogo jogo;
    private Conta conta; 
    /* Ainda não sei se todo tipo de conta vai ter essa relação usuário/jogo 
    - acho que sim victin, usuario tem acesso aos jogos que ele comprar, dev tem acesso aos jogos que ele criar, 
    e operador tem acesso a todos os jogos
    */
    private double horasJogadas;

    public Registro(int id, Jogo jogo, Conta conta, double horasJogadas){
        
        super(id);
        this.jogo = jogo;
        this.conta = conta;
        this.horasJogadas = horasJogadas; 
    }

    public void setJogo(Jogo jogo) { this.jogo = jogo; }
    public void setConta(Conta conta) { this.conta = conta; }
    public void setHorasJogadas( double horasJogadas) { this.horasJogadas = horasJogadas;}

    public Jogo getJogo() { return this.jogo; }
    public Conta getConta() { return conta; }
    public double getHorasJogadas() { return horasJogadas; }

    public boolean salvar(ArrayList<Registro> listaRegistros){

        if(this.foiSalvo){
            return false;
        }

        listaRegistros.add(this);
        this.setFoiSalvo(true);
        Gerenciador.salvarRegistro(listaRegistros);
        return true;
    }

    public boolean atualizar(ArrayList<Registro> listaRegistros){

        if(!this.foiSalvo){
            return false;
        }

        Gerenciador.salvarRegistro(listaRegistros);
        return true;
    }

    public static boolean apagar(int id, ArrayList<Registro> listaRegistros){

        for(int i = 0; i < listaRegistros.size(); i++){

            if(id == listaRegistros.get(i).getId()){

                Registro apagado = listaRegistros.get(i);
                apagado.setFoiSalvo(false);
                listaRegistros.remove(i);
                Gerenciador.salvarRegistro(listaRegistros);
                return true;
            }
        }

        return false;
    }

    public boolean carregar(int id, ArrayList<Registro> listaRegistros){

        for(int i = 0; i < listaRegistros.size(); i++){

            if(id == listaRegistros.get(i).getId()){

                this.id = listaRegistros.get(i).getId();
                this.jogo = listaRegistros.get(i).getJogo();
                this.conta = listaRegistros.get(i).getConta();
                this.horasJogadas = listaRegistros.get(i).getHorasJogadas();
                this.setFoiSalvo(true);

                return true;
            }
        }

        return false;
    }

    public ArrayList<Registro> carregarTodos(ArrayList<Conta> listaContas, ArrayList<Jogo> listaJogos) {
        return Gerenciador.CarregaRegistros(listaJogos, listaContas);
    }

    @Override
    public String toString(){
        return String.format("\n%s, Jogo: %s, Conta: %s", super.toString(), jogo.toString(), conta.toString());
    }
    
}