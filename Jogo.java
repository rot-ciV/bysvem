public class Jogo extends Bysvem{

    private  String nome;
    private String genero;
    private String desenvolvedora;
    private double preco;

    public Jogo(){

        super();
        this.nome = "";
        this.genero = "";
        this.desenvolvedora = "";
        this.preco = 0.0;
    }

    public Jogo(int id, String nome, String genero, String desenvolvedora, double preco){

        super(id);
        this.nome = nome;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.preco = preco;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDesenvolvedora(String desenvolverdora) { this.desenvolvedora = desenvolverdora; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getNome() { return this.nome; }
    public String getGenero() { return this.genero; }
    public String getDesenvolvedora() { return this.desenvolvedora; }
    public double getPreco() { return this.preco; }

}