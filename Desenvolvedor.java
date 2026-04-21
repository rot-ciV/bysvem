
public class Desenvolvedor extends Conta{

    protected String desenvolvedora;

    public void setDesenvolvedora(String desenvolvedora) { this.desenvolvedora = desenvolvedora; }
    public String getDesenvolvedora() { return desenvolvedora; }

    public Desenvolvedor(int id, String nome, int senha, String desenvolvedora){
        
        super(id,nome,senha);
        this.desenvolvedora = desenvolvedora;
    }

    public void criaJogo(int id, String nome, String genero, double preco, String disc){

        Jogo novoJogo = new Jogo(id, nome, genero, this.desenvolvedora, preco, disc);

    }

}

 