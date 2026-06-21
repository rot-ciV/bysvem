package bysvem.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    // public boolean comprarJogo(Jogo jogoComprado, ArrayList<Conta> contas, ArrayList<Registro> registros, Loja loja) {
    //     if (getSaldo() < jogoComprado.getPreco()) return false;

    //     double novoSaldo = getSaldo() - jogoComprado.getPreco();
    //     setSaldo(novoSaldo);
    //     for (Conta c : contas) {
    //         if (c.getId() == getId()) {
    //             ((Usuario) c).setSaldo(novoSaldo);
    //             break;
    //         }
    //     }

    //     int idItem = loja.criaId(3);
    //     ItemCompra item = new ItemCompra(idItem, jogoComprado, TipoAquisicao.COMPRA_DEFINITIVA, jogoComprado.getPreco());

    //     int idCompra = loja.criaId(4);
    //     List<ItemCompra> itens = new ArrayList<>();
    //     itens.add(item);
    //     Compra compra = new Compra(idCompra, this, LocalDate.now(), itens);

    //     adicionarCompra(compra);

    //     int idReg = loja.criaId(1);
    //     Registro registro = new Registro(idReg, jogoComprado, this, 0.0);
    //     registro.salvar(registros);

    //     if (atualizar(contas)) {
    //         Gerenciador.salvarTodasCompras(contas);
    //         return true;
    //     }
    //     return false;
    // }

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
    public boolean carregar(int id, ArrayList<Conta> listaContas) {
        for (Conta c : listaContas) {
            if (c.getId() == id) {
                super.carregar(id, listaContas);
                Usuario carregado = (Usuario) c;
                this.saldo = carregado.getSaldo();
                this.compras = new ArrayList<>(carregado.getCompras());
                // carrinho não é carregado (é temporário)
                return true;
            }
        }
        return false;
    }

    public void compraSaldo(ArrayList<Conta> contas, Usuario usuario, double valor, Scanner scn) {
        double saldoAtual = usuario.getSaldo();
        System.out.printf("\nSaldo anterior: R$%.2f\nNovo saldo: R$%.2f\n\n", saldoAtual, saldoAtual + valor);
        System.out.println("Deseja confirmar:\n1 - Confirmar\n2 - Voltar");

        while (true) {
            String opcaoString = scn.nextLine();
            if (Util.entradaInt(opcaoString)) {
                int opcao = Integer.parseInt(opcaoString);
                if (opcao == 1) {
                    double saldoAntigo = this.saldo;
                    for (int i = 0; i < contas.size(); i++) {
                        if (contas.get(i).getId() == usuario.getId()) {
                            ((Usuario) contas.get(i)).setSaldo(saldoAtual + valor);
                            this.setSaldo(saldoAtual + valor);
                            if (this.atualizar(contas)) {
                                System.out.println("Saldo atualizado com sucesso!");
                            } else {
                                System.out.println("Erro ao atualizar saldo.");
                                ((Usuario) contas.get(i)).setSaldo(saldoAntigo);
                                this.setSaldo(saldoAntigo);
                            }
                            return;
                        }
                    }
                } else if (opcao == 2) {
                    System.out.println("Operação cancelada.");
                    return;
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }
            } else {
                System.out.println("Digite um número válido.");
            }
        }
    }

    @Override
    public String toString() {
        return String.format("\n%s\nSaldo: %.2f", super.toString(), saldo);
    }
}