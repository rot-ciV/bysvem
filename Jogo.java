public class Jogo extends Bysvem{

    protected String nome;
    protected String genero;
    protected String desenvolvedora;
    protected double preco;
    protected double horasJogadas; // Coloca aqui ou em usuário?


    public Jogo(){

        super();
        this.nome = "";
        this.genero = "";
        this.desenvolvedora = "";
        this.preco = 0.0;
        this.horasJogadas = 0.0;
    }

    public Jogo(int id, String nome, String genero, String desenvolvedora, double preco, double horasJogadas){

        super(id);
        this.nome = nome;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.preco = preco;
        this.horasJogadas = horasJogadas;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDesenvolvedora(String desenvolverdora) { this.desenvolvedora = desenvolverdora; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setHorasJogadas(double horasJogadas) { this.horasJogadas = horasJogadas; }

    public String getNome() { return this.nome; }
    public String getGenero() { return this.genero; }
    public String getDesenvolvedora() { return this.desenvolvedora; }
    public double getPreco() { return this.preco; }
    public double getHorasJogadas() { return this.horasJogadas; }

    @Override
    public void salvar(){

        this.foiSalvo = true;
    }
}