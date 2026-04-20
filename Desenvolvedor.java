import java.util.ArrayList;

public class Desenvolvedor extends Conta{

    protected String desenvolvedora;
    protected ArrayList<Jogo> jogosEmDesenvolvimento;
    protected ArrayList<Jogo> jogosPublicados;
    // (Victor) Diferenciei jogos que estão disponíveis para serem comprados e que ainda estão em desenvolvimento.


    public void setDesenvolvedora(String desenvolvedora) { this.desenvolvedora = desenvolvedora; }
    public String getDesenvolvedora() { return desenvolvedora; }

    public Desenvolvedor(int id, String nome, int senha, String desenvolvedora){
        
        super(id,nome,senha);
        this.desenvolvedora = desenvolvedora;
        this.jogosEmDesenvolvimento = new ArrayList<>();
        this.jogosPublicados = new ArrayList<>();
    }

    // (Victor) Essa função apenas cria o jogo, mas ele ainda não está disponível na loja
    public void criaJogo(int id, String nome, String genero, double preco){

        Jogo novoJogo = new Jogo(id, nome, genero, this.desenvolvedora, preco);
        jogosEmDesenvolvimento.add(novoJogo);
    }

    /* (Victor) Você dá o id do jogo que quer postar, caso o id exista na lista de jogos em desenv ele coloca o objeto
    jogo na lista de jogos publicados. O boolean é apenas um retorno para saber se colocou ou não o jogo na lista publicados */ 
    public boolean postaJogo(int id){

        for(int i = 0; i < this.jogosEmDesenvolvimento.size(); i++){

            Jogo buscaJogo = this.jogosEmDesenvolvimento.get(i);

            if(buscaJogo.id == id){

                this.jogosPublicados.add(buscaJogo);
                this.jogosEmDesenvolvimento.remove(i);
                
                return true;
            }
        }

        return false;
    }
}