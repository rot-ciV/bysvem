package bysvem.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra extends Entidade {
    
    private transient Usuario usuario;   // Isso é para nao salvar no arquivo
    private int idUsuario;               // Ja que usuario n é mais salvo, usamos esse id para refazer a ligacao do usuario com a conta
    private LocalDate dataCompra;
    private List<ItemCompra> itens;
    private double valorTotal;

    public Compra(int id, Usuario usuario, LocalDate dataCompra, List<ItemCompra> itens) {
        super(id);
        setUsuario(usuario);
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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        this.idUsuario = (usuario != null) ? usuario.getId() : 0;
    }
    public int getIdUsuario() { return idUsuario; }

    public LocalDate getDataCompra() { return dataCompra; }
    public List<ItemCompra> getItens() { return itens; }
    public double getValorTotal() { return valorTotal; }

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