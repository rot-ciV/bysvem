package bysvem.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private int id;
    private Usuario usuario;
    private LocalDate dataCompra;
    private List<ItemCompra> itens;
    private double valorTotal;

    // Construtor com cálculo automático do total
    public Compra(int id, Usuario usuario, LocalDate dataCompra, List<ItemCompra> itens) {
        this.id = id;
        this.usuario = usuario;
        this.dataCompra = dataCompra;
        this.itens = itens != null ? itens : new ArrayList<>();
        this.valorTotal = calcularTotal();
    }

    private double calcularTotal() {
        double total = 0;
        for (ItemCompra item : itens) {
            total += item.getPrecoPago();
        }
        return total;
    }

    // Getters e Setters
    public int getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public LocalDate getDataCompra() { return dataCompra; }
    public List<ItemCompra> getItens() { return itens; }
    public double getValorTotal() { return valorTotal; }

    public void setId(int id) { this.id = id; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public void setItens(List<ItemCompra> itens) {
        this.itens = itens;
        this.valorTotal = calcularTotal();
    }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public void adicionarItem(ItemCompra item) {
        itens.add(item);
        this.valorTotal = calcularTotal();
    }
}