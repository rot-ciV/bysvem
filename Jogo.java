

import java.util.ArrayList;

public class Jogo extends Entidade<Jogo>{

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

    public boolean salvar(ArrayList<Jogo> listaJogos){

        if(this.foiSalvo){
            return false;
        }

        listaJogos.add(this);
        this.setFoiSalvo(true);
        Gerenciador.salvarJogos(listaJogos);
        return true;
    }
    
    public boolean atualizar(ArrayList<Jogo> listaJogos){

        if(!this.foiSalvo){
            return false;
        }

        Gerenciador.salvarJogos(listaJogos);
        return true;
    }

    public boolean apagar(int id, ArrayList<Jogo> listaJogos){

        for(int i = 0; i < listaJogos.size(); i++){

            if(id == listaJogos.get(i).getId()){

                Jogo apagado = listaJogos.get(i);
                apagado.setFoiSalvo(false);
                listaJogos.remove(i);
                Gerenciador.salvarJogos(listaJogos);
                return true;
            }
        }

        return false;
    }

    public boolean carregar(int id, ArrayList<Jogo> listaJogos){

        for(int i = 0; i < listaJogos.size(); i++){

            if(id == listaJogos.get(i).getId()){

                this.id = listaJogos.get(i).getId();
                this.nome = listaJogos.get(i).getNome();
                this.genero = listaJogos.get(i).getGenero();
                this.desenvolvedora = listaJogos.get(i).getDesenvolvedora();
                this.preco = listaJogos.get(i).getPreco();
                this.desc = listaJogos.get(i).getDesc();
                this.setFoiSalvo(true);

                return true;
            }
        }

        return false;
    }

    @Override
    public ArrayList<Jogo> carregarTodos() {

        ArrayList<Jogo> jogos = Gerenciador.carregaJogos();

        for(Jogo j : jogos){

            j.setFoiSalvo(true);
        }

        return jogos;
    }

    @Override
    public String toString(){
        return String.format("\n%s, Nome: %s, Genero: %s, Desenvolvedora: %s, Preço: %ff, Descrição: %s", super.toString(), nome, genero, desenvolvedora, preco, desc);
    }

}