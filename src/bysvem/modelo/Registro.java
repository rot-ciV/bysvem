package bysvem.modelo;

public class Registro extends Entidade{

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

    @Override
    public String toString(){
        return String.format("\n%s, Jogo: %s, Conta: %s", super.toString(), jogo.toString(), conta.toString());
    }
}