package bysvem.modelo;

import bysvem.visao.*;
import java.util.ArrayList;
import java.util.Scanner;   

public class ItemCompra{
    private int id;
    private Jogo jogo;
    private TipoAquisicao tipo;
    //Isso aqui coloquei, mas ainda não aprendi direito como mexe nesse enum, a ideia é essa aqui por exemplo:
    //TipoAquisicao tipo = TipoAquisicao.COMPRA_DEFINITIVA;
    // if (tipo == TipoAquisicao.ASSINATURA) System.out.println("Cobrança mensal recorrente");
    private double precoPago;
    //private Assinatura assinatura;

    public ItemCompra(int id, Jogo jogo, TipoAquisicao tipo, double precoPago){
        this.id = id;
        this.jogo = jogo;
        this.tipo = tipo;
        this.precoPago = precoPago;
    }

    public void setId(int id){ this.id = id;}
    public void setJogo(Jogo jogo){ this.jogo = jogo;}
    public void setTipoAquisicao(TipoAquisicao tipo){ this.tipo = tipo;}
    public void setPrecoPago(double precoPago){ this.precoPago = precoPago;}

    public int getId(){ return this.id;}
    public Jogo getJogo(){ return this.jogo;}
    public TipoAquisicao getTipoAquisicao(){ return this.tipo;}
    public double getPrecoPago(){ return this.precoPago;}
}