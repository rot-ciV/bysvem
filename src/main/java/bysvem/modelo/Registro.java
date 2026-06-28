package bysvem.modelo;

public class Registro extends Entidade {

    private transient Conta conta;
    private int idConta;
    private transient Jogo jogo;
    private int idJogo;
    private double horasJogadas;

    public Registro(int id, Jogo jogo, Conta conta, double horasJogadas) {
        super(id);
        setJogo(jogo);
        setConta(conta);
        this.horasJogadas = horasJogadas;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
        this.idJogo = (jogo != null) ? jogo.getId() : 0;
    }
    public void setConta(Conta conta) {
        this.conta = conta;
        this.idConta = (conta != null) ? conta.getId() : 0;
    }

    public Jogo getJogo() { return jogo; }
    public int getIdJogo() { return idJogo; }
    public Conta getConta() { return conta; }
    public int getIdConta() { return idConta; }
    public double getHorasJogadas() { return horasJogadas; }
    public void setHorasJogadas(double horasJogadas) { this.horasJogadas = horasJogadas; }

    @Override
    public String toString() {
        return String.format("\n%s, Jogo: %s, Conta: %s", super.toString(), jogo, conta);
    }
}