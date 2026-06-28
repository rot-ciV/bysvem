package bysvem.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class Usuario extends Conta {
    private double saldo;
    private transient List<Compra> compras;
    private transient List<ItemCompra> carrinho;

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban) {
        super(id, nome, senha, email, ban);
        this.saldo = saldo;
        this.compras = new ArrayList<>();
        this.carrinho = new ArrayList<>();
    }

    public double getSaldo() { return saldo; }
    public List<Compra> getCompras() {
        if (compras == null) {
            compras = new ArrayList<>();
        }
        return compras;
    }

    public List<ItemCompra> getCarrinho() {
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        return carrinho;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setCompras(List<Compra> compras) {
        this.compras = new ArrayList<>(compras);
    }

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
    for (ItemCompra item : getCarrinho()) {
        if (item.getJogo().getId() == jogo.getId()) return true;
    }
    return false;
}

    public void adicionarCompra(Compra compra) {
        if (compra != null && !compras.contains(compra)) {
            compras.add(compra);
        }
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

    public void finalizarCompra() throws PersistenceException {
        if (carrinho.isEmpty()) {
            throw new IllegalStateException("Carrinho vazio!");
        }
        double total = getTotalCarrinho();
        if (saldo < total) {
            throw new IllegalStateException("Saldo insuficiente!");
        }
        saldo -= total;

        int idCompra = IdUtil.gerarIdCompra();
        Compra compra = new Compra(idCompra, this, LocalDate.now(), new ArrayList<>(carrinho));
        adicionarCompra(compra);

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);
        EntidadeDAO<Registro> registroDAO = gp.getDAO(Registro.class);
        EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);

        compraDAO.salvar(compra);

        for (ItemCompra item : carrinho) {
            Registro registro = new Registro(IdUtil.gerarIdCompra(), item.getJogo(), this, 0.0);
            registroDAO.salvar(registro);
        }

        try {
            contaDAO.atualizar(this);
        } catch (PersistenceException e) {
            contaDAO.salvar(this);
        }
        contaDAO.persistir("dados/contas.dat");
        compraDAO.persistir("dados/compras.dat");
        registroDAO.persistir("dados/registros.dat");

        carrinho.clear();
    }

    @Override
    public String toString() {
        return String.format("\n%s\nSaldo: %.2f", super.toString(), saldo);
    }
}