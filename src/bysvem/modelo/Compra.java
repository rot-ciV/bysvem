package bysvem.modelo;

import bysvem.visao.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Compra {
    private int id;
    private Usuario usuario;
    private LocalDate dataCompra;
    private List<ItemCompra> itens;   // ← lista de itens (carrinho)
    private double valorTotal;

    public Compra(Usuario usuario, List<ItemCompra> itens, double valorTotal){
        this.id = geraIdCompra();//tem que fazer essa função ainda
        this.usuario = usuario;
        this.dataCompra = LocalDate.now();
        this.itens = itens;
        this.valorTotal = valorTotal;
    }

    public void setId(int id){ this.id = id;}
    public void setUsuario(Usuario usuario){ this.usuario = Usuario;}
    public void setDataCompra(LocalDate dataCompra){this.dataCompra = dataCompra;}
    public void setItens(List<ItemCompra> itens){ this.itens = itens;}
    public void setValorTotal(double valorTotal){ this.valorTotal = valorTotal;}

    public int getId(){ return this.id;}
    public Usuario getUsuario(){ return this.usuario;}
    public LocalDate getDataCompra(){ return this.dataCompra;}
    public List<ItemCompra> getItens(){ return this.itens;}
    public double getValorTotal(){return this.valorTotal;}

    public void adicionarItem(ItemCompra item) {
        itens.add(item);
    }
}