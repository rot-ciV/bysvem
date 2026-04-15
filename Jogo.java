public class Jogo extends Bysvem{

    private  String nome;
    private String genero;
    private String desenvolvedora;
    private double preco;
    private double horasJogadas; // Coloca aqui ou em usuário?
    private int copiasVendidas;


    public Jogo(){

        super();
        this.nome = "";
        this.genero = "";
        this.desenvolvedora = "";
        this.preco = 0.0;
        this.horasJogadas = 0.0;
        this.copiasVendidas = 0;
    }

    public Jogo(int id, String nome, String genero, String desenvolvedora, double preco, double horasJogadas){

        super(id);
        this.nome = nome;
        this.genero = genero;
        this.desenvolvedora = desenvolvedora;
        this.preco = preco;
        this.horasJogadas = horasJogadas;
        this.copiasVendidas = 0;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setGenero(String genero) { this.genero = genero; }
    public void setDesenvolvedora(String desenvolverdora) { this.desenvolvedora = desenvolverdora; }
    public void setPreco(double preco) { this.preco = preco; }
    public void setHorasJogadas(double horasJogadas) { this.horasJogadas = horasJogadas; }
    public void setCopiasVendidas(int copiasVendidas) { this.copiasVendidas = copiasVendidas; }

    public String getNome() { return this.nome; }
    public String getGenero() { return this.genero; }
    public String getDesenvolvedora() { return this.desenvolvedora; }
    public double getPreco() { return this.preco; }
    public double getHorasJogadas() { return this.horasJogadas; }
    public double getCopiasVendidas() { return this.copiasVendidas; }

    @Override
    public void salvar(){

        this.foiSalvo = true;
    }

    public void editaNome(Jogo jogo, String nome){

        jogo.nome = nome;
    }

    public void editaGenero(Jogo jogo, String genero){

        jogo.genero = genero;
    }

    //Nao pode editar a desenvolvedora pq o desenvolvedor já vai ser de uma empresa

    public void editaPreco(Jogo jogo, double preco){

        jogo.preco = preco;
    }
}