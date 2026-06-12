package bysvem.modelo;

import bysvem.visao.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Month;

public class Compra {
    private int id;
    private Usuario usuario;
    private LocalDate dataCompra;
    private List<ItemCompra> itens;   // ← lista de itens (carrinho)
    private double valorTotal;

    public Compra(Usuario usuario, List<ItemCompra> itens, double valorTotal){
        this.id = 4;//geraIdCompra();//tem que fazer essa função ainda
        this.usuario = usuario;
        this.dataCompra = LocalDate.now();
        this.itens = itens;
        this.valorTotal = valorTotal;
    }

    public void setId(int id){ this.id = id;}
    public void setUsuario(Usuario usuario){ this.usuario = usuario;}
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

    // public boolean aquisição(Conta conta, Jogo escolhido, TipoAquisicao tda){
    //     //A ideia que pensei aqui seria ter um botão de aquisição do jogo, dai clicando nele aparece 2 opções, sendo um "Comprar Jogo" e outro "Verificar assinaturas"
    //     try{
    //         if(tda == TipoAquisicao.COMPRA_DEFINITIVA){
    //             return Carrinho(escolhido);
    //         } if(tda == TipoAquisicao.ASSINATURA){
    //             return Assinar();
    //         }
    //     } catch (Exception e){
    //         System.out.println("Error in aquisition type");
    //     }
    // }

    // public boolean Carrinho(Jogo escolhido){

    // }

    


    //     System.out.println("Gostaria de adquirir o jogo?\n1 - Comprar\n2 - Cancelar");
    //     int resposta_compra = -1;
    //     String resposta_compraString = scn.nextLine();
    //     if(entradaInt(resposta_compraString)){
    //         resposta_compra = Integer.parseInt(resposta_compraString);
    //     }
    //     if(resposta_compra == 1){
    //         if(((Usuario)conta).compraJogo(escolhido, contas, registros, this)){                               
    //             System.out.println("Jogo adquirido com sucesso!");
    //             return false;
    //         }else{
    //             System.out.println("\nSaldo insuficiente!\n\n1 - Adicionar saldo\n2 - Voltar para os jogos disponíveis");
    //             int resposta_saldo;
    //             String resposta_saldoString;
    //             while(true){
    //                 resposta_saldoString = scn.nextLine();
    //                 if(entradaInt(resposta_saldoString)){
    //                     resposta_saldo = Integer.parseInt(resposta_saldoString);
    //                     break;
    //                 }else{
    //                     System.out.println("Opção inválida, digite 1 ou 2.");
    //                 }
    //             }
    //             if(resposta_saldo == 1){
    //                 System.out.print("\nInforme o valor que você deseja adicionar: ");
    //                 double valorSaldo = 0.0;
    //                 String valor = "";
    //                 while(true){
    //                     valor = scn.nextLine();
    //                     if(entradaDouble(valor)){
    //                         valorSaldo = Double.parseDouble(valor);
    //                         break;
    //                     }else{
    //                         System.out.println("Digite um valor válido.");
    //                     }
    //                 }
    //                 Usuario user = (Usuario)conta;
    //                 user.compraSaldo(contas, user, valorSaldo,scn);
    //                 return false;
    //             }else if(resposta_saldo == 2){
    //                 return true;
    //             }else{
    //                 System.out.println("Opção inválida");
    //                 return false;
    //             }
    //         }
    //     }else if(resposta_compra == 2){
    //         return true;
    //     }else{
    //         System.out.println("Opção inválida.");
    //         return false;
    //     }
        
    // }

}