package bysvem.modelo;

public class ItemCompra {
    private int id;
    private Jogo jogo;
    private TipoAquisicao tipo;
    private double precoPago;
    private Assinatura assinatura; // pode ser null se for compra definitiva

    public ItemCompra(int id, Jogo jogo, TipoAquisicao tipo, double precoPago) {
        this.id = id;
        this.jogo = jogo;
        this.tipo = tipo;
        this.precoPago = precoPago;
        this.assinatura = null;
    }

    public ItemCompra(int id, Jogo jogo, TipoAquisicao tipo, double precoPago, Assinatura assinatura) {
        this.id = id;
        this.jogo = jogo;
        this.tipo = tipo;
        this.precoPago = precoPago;
        this.assinatura = assinatura;
    }

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public TipoAquisicao getTipoAquisicao() { return tipo; }
    public void setTipoAquisicao(TipoAquisicao tipo) { this.tipo = tipo; }

    public double getPrecoPago() { return precoPago; }
    public void setPrecoPago(double precoPago) { this.precoPago = precoPago; }

    public Assinatura getAssinatura() { return assinatura; }
    public void setAssinatura(Assinatura assinatura) { this.assinatura = assinatura; }
}