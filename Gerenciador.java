import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Gerenciador extends Bysvem{

    private static final String CAMINHO_CONTAS = "dados/contas.txt";
    private static final String CAMINHO_JOGOS = "dados/jogos.txt";
    private static final String CAMINHO_REGISTROS = "dados/registros.txt";
    // "final" declara uma constante, logo todos os caminhos nunca serão alterados.

    // private para que ninguém consiga criar um objeto Gerenciador
    private Gerenciador() {}

    public static void salvarJogo(ArrayList<Jogo> listaDeJogos){

        try {
            FileWriter arquivo = new FileWriter(CAMINHO_JOGOS);
            BufferedWriter escritor = new BufferedWriter(arquivo);


            for(int i = 0; i < listaDeJogos.size(); i++){

                Jogo jogoAtual = listaDeJogos.get(i);

                String linha = jogoAtual.getId() + ";" + jogoAtual.getNome() + ";" + jogoAtual.getGenero() + ";" + jogoAtual.getDesenvolvedora() + ";" + jogoAtual.getPreco();
                escritor.write(linha);
                escritor.newLine();

            }

            escritor.close();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());
        }
    }




}