public class Registro extends Bysvem{

    private Jogo jogo;
    private Conta conta; // Ainda não sei se todo tipo de conta vai ter essa relação usuário/jogo
    private double horasJogadas;

    public Registro(int id, Jogo jogo, Conta conta){
        
        super(id);
        this.jogo = jogo;
        this.conta = conta;
        this.horasJogadas = 0.0; 
    }

    public void setJogo(Jogo jogo) { this.jogo = jogo; }
    public void setConta(Conta conta) { this.conta = conta; }
    public void setHorasJogadas( double horasJogadas) { this.horasJogadas = horasJogadas;}

    public Jogo getJogo() { return this.jogo; }
    public Conta getConta() { return conta; }
    public double getHorasJogadas() { return horasJogadas; }
    
}