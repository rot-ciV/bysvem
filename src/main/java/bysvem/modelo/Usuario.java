package bysvem.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario extends Conta {
    private double saldo;
    private List<Compra> compras;
    private List<ItemCompra> carrinho; 

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban) {
        super(id, nome, senha, email, ban);
        this.saldo = saldo;
        this.compras = new ArrayList<>();
        this.carrinho = new ArrayList<>(); 
    }

    // Getters e Setters
    public double getSaldo() { return saldo; }
    public List<Compra> getCompras() { return compras; }
    public List<ItemCompra> getCarrinho() { return carrinho; } 

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setCompras(List<Compra> compras) { this.compras = compras; }

    // Métodos do carrinho
    public void adicionarAoCarrinho(ItemCompra item) {
        if (item != null) carrinho.add(item);
    }

    public void removerDoCarrinho(ItemCompra item) {
        carrinho.remove(item);
    }

    public void limparCarrinho() {
        carrinho.clear();
    }

    public double getTotalCarrinho() {
        double total = 0;
        for (ItemCompra item : carrinho) {
            total += item.getPrecoPago();
        }
        return total;
    }

    public boolean jogoNoCarrinho(Jogo jogo) {
        for (ItemCompra item : carrinho) {
            if (item.getJogo().getId() == jogo.getId()) return true;
        }
        return false;
    }

    public void adicionarCompra(Compra compra) {
        if (compra != null) compras.add(compra);
    }

    public ArrayList<Jogo> biblioteca() {
        ArrayList<Jogo> biblioteca = new ArrayList<>();
        for (Compra compra : compras) {
            for (ItemCompra item : compra.getItens()) {
                biblioteca.add(item.getJogo());
            }
        }
        return biblioteca;
    }

    @Override
    public String toString() {
        return String.format("\n%s\nSaldo: %.2f", super.toString(), saldo);
    }
}