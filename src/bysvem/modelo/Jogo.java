package bysvem.modelo;

public class Jogo extends Entidade{

    private String nome;
    private String genero;
    private String desenvolvedora;
    private double preco;
    private String desc;

    public Jogo(){

        super();
        this.nome = "";
        this.genero = "";
        this.desenvolvedora = "";
        this.preco = 0.0;
        this.desc = "";
    }

    public Jogo(int id, String nome, String genero, String desenvolvedora, double preco, String miniDesc){

        super(id);
        this.nome = nome;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.preco = preco;
        this.desc = miniDesc;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDesenvolvedora(String desenvolverdora) { this.desenvolvedora = desenvolverdora; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setDesc(String miniDesc) { this.desc = miniDesc; }

    public String getNome() { return this.nome; }
    public String getGenero() { return this.genero; }
    public String getDesenvolvedora() { return this.desenvolvedora; }
    public double getPreco() { return this.preco; }
    public String getDesc() { return this.desc; }

    @Override
    public String toString(){
        return String.format("\n%s, Nome: %s, Genero: %s, Desenvolvedora: %s, Preço: %ff, Descrição: %s", super.toString(), nome, genero, desenvolvedora, preco, desc);
    }

}