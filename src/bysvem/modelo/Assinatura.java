package bysvem.modelo;

import bysvem.visao.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Month;

public enum TipoAssinatura {
    BASICA,
    PREMIUM,
    ULTIMATE
}

public class Assinatura {
    private LocalDate dataInicio;
    private LocalDate dataFim;      // ou duração em dias
    //LocalDate ->  (AAAA-MM-DD) (É uma ferramenta do próprio java rapaziada)
    //Pegar a data de hoje : ; LocalDate hoje = LocalDate.now();
    //Colocar uma data específica : LocalDate aniversario = LocalDate.of(1995, Month.OCTOBER, 20);
    private boolean autoRenovação;
    private TipoAssinatura tipo;

    public Assinatura(LocalDate dataFim, TipoAssinatura tipo){
        this.dataInicio = LocalDate.now();
        this.dataFim = dataFim;
        this.autoRenovação = true; //fiz essa similar a gamepass, onde sempre começa com reenovação automática
        this.tipo = tipo
    }

    public void setDataInicio(LocalDate dataInicio){ this.dataInicio = dataInicio}
    public void setDataFim(LocalDate dataFim){ this.dataFim = dataFim;}
    public void setAutoRenovação(boolean autoRenovação){ this.autoRenovação = autoRenovação;}
    public void setTipo(TipoAssinatura tipo){ this.tipo = tipo;}

    public LocalDate getDataInicio(){ return this.dataInicio;}
    public LocalDate getDataFim(){ return this.dataFim;}
    public boolean getAutoRenovação(){ return this.autoRenovação;}
    public TipoAssinatura getTipo(){ return this.tipo;}

}