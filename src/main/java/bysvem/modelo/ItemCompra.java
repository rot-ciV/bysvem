package bysvem.modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class ItemCompra implements Serializable{
    private int id;
    private Jogo jogo;
    private double precoPago;
    private LocalDate dataAtivacao; 

    public ItemCompra(int id, Jogo jogo, double precoPago) {
        this.id = id;
        this.jogo = jogo;
        this.precoPago = precoPago;
        this.dataAtivacao = LocalDate.now();
    }

    public ItemCompra(int id, Jogo jogo, double precoPago, LocalDate dataAtivacao) {
        this.id = id;
        this.jogo = jogo;
        this.precoPago = precoPago;
        this.dataAtivacao = dataAtivacao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public double getPrecoPago() { return precoPago; }
    public void setPrecoPago(double precoPago) { this.precoPago = precoPago; }

    public LocalDate getDataAtivacao() { return dataAtivacao; }
    public void setDataAtivacao(LocalDate dataAtivacao) { this.dataAtivacao = dataAtivacao; }
}