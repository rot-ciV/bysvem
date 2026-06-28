package bysvem.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

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

    /**
     * Substitui a lista de compras por uma nova lista (evita duplicatas ao carregar do arquivo).
     */
    public void setCompras(List<Compra> compras) {
        this.compras = new ArrayList<>(compras);
    }

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

    /**
     * Adiciona uma compra à lista, evitando duplicatas (compara por ID).
     */
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

    /**
     * Finaliza a compra do carrinho atual, persistindo os dados em arquivo.
     * @throws PersistenceException se ocorrer erro de persistência
     * @throws IllegalStateException se o carrinho estiver vazio ou saldo insuficiente
     */
    public void finalizarCompra() throws PersistenceException {
        if (carrinho.isEmpty()) {
            throw new IllegalStateException("Carrinho vazio!");
        }

        double total = getTotalCarrinho();
        if (saldo < total) {
            throw new IllegalStateException("Saldo insuficiente!");
        }

        saldo -= total;

        int idCompra = gerarId();
        Compra compra = new Compra(idCompra, this, LocalDate.now(), new ArrayList<>(carrinho));
        adicionarCompra(compra); 

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);
        EntidadeDAO<Registro> registroDAO = gp.getDAO(Registro.class);
        EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);

        compraDAO.salvar(compra);

        for (ItemCompra item : carrinho) {
            Registro registro = new Registro(gerarId(), item.getJogo(), this, 0.0);
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

    private static int gerarId() {
        return (int) (Math.random() * 100000);
    }

    @Override
    public String toString() {
        return String.format("\n%s\nSaldo: %.2f", super.toString(), saldo);
    }
}