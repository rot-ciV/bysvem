package bysvem.modelo;

import java.time.LocalDate;

public class Assinatura {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean autoRenovacao;
    private TipoAssinatura tipo;

    // Construtor para uso normal (data fim e tipo)
    public Assinatura(LocalDate dataFim, TipoAssinatura tipo) {
        this.dataInicio = LocalDate.now();
        this.dataFim = dataFim;
        this.autoRenovacao = true;
        this.tipo = tipo;
    }

    // Construtor para carregamento (com data início explícita)
    public Assinatura(LocalDate dataInicio, LocalDate dataFim, TipoAssinatura tipo) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.autoRenovacao = true;
        this.tipo = tipo;
    }

    // getters e setters
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public boolean getAutoRenovacao() { return autoRenovacao; }
    public void setAutoRenovacao(boolean autoRenovacao) { this.autoRenovacao = autoRenovacao; }

    public TipoAssinatura getTipo() { return tipo; }
    public void setTipo(TipoAssinatura tipo) { this.tipo = tipo; }
}