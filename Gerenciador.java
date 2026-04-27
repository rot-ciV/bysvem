
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Gerenciador{

    private static final String CAMINHO_CONTAS = "dados/contas.txt";
    private static final String CAMINHO_JOGOS = "dados/jogos.txt";
    private static final String CAMINHO_REGISTROS = "dados/registros.txt";
    // "final" declara uma constante, logo todos os caminhos nunca serão alterados.

    // private para que ninguém consiga criar um objeto Gerenciador
    private Gerenciador() {}

    public static void salvarJogos(ArrayList<Jogo> listaDeJogos){

        try {

            FileWriter arquivo = new FileWriter(CAMINHO_JOGOS);
            //prepara o arquivo txt para ser escrito
            BufferedWriter escritor = new BufferedWriter(arquivo);
            // vai salvando os caracteres em uma memória temporária, ao ser fechada escreve todas no arquivo.

            for(int i = 0; i < listaDeJogos.size(); i++){

                Jogo jogoAtual = listaDeJogos.get(i);

                String linha = jogoAtual.getId() + ";" + jogoAtual.getNome() + ";" + jogoAtual.getGenero() + ";" + jogoAtual.getDesenvolvedora() + ";" + jogoAtual.getPreco() + ";" + jogoAtual.getDesc();
                escritor.write(linha);
                escritor.newLine();

            }

            escritor.close();

        }catch(IOException e){

            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static ArrayList<Jogo> carregaJogos(){

        ArrayList<Jogo> jogosCarregados = new ArrayList<>();

        try {
            
            FileReader arquivo = new FileReader(CAMINHO_JOGOS);
            BufferedReader leitor = new BufferedReader(arquivo);
            
            String linhaAtual = leitor.readLine();

            while(linhaAtual != null){
                
                String[] dados = linhaAtual.split(";");
                Jogo jogoAtual = new Jogo(Integer.parseInt(dados[0]), dados[1], dados[2], dados[3], Double.parseDouble(dados[4]), dados[5]);
                jogosCarregados.add(jogoAtual);
                
                linhaAtual = leitor.readLine();
            }

            leitor.close();

        } catch (Exception e) {

            System.out.println("Erro de leitura no arquivo jogos.txt");
            // se cair nesse caso ainda há o return?
        }
        
        return jogosCarregados;
    }
  
    public static void salvarContas(ArrayList<Conta> listaContas){

        try {
            
            FileWriter arquivo = new FileWriter(CAMINHO_CONTAS);
            BufferedWriter escritor = new BufferedWriter(arquivo);

            for(int i = 0; i < listaContas.size(); i++){

                Conta conta_atual = listaContas.get(i);

                if(conta_atual instanceof Usuario){

                    String linha = null;

                    if(conta_atual.getBan()){
                        linha = "BAN_USR;" + conta_atual.getId() + ";" + conta_atual.getNome() + ";" + conta_atual.getSenha() + ";" + conta_atual.getEmail() + ";" + ((Usuario) conta_atual).getSaldo() + ";" + conta_atual.getBan();
                    }else{
                        linha = "USR;" + conta_atual.getId() + ";" + conta_atual.getNome() + ";" + conta_atual.getSenha() + ";" + conta_atual.getEmail() + ";" + ((Usuario) conta_atual).getSaldo() + ";" + conta_atual.getBan();
                    }

                    escritor.write(linha);
                    escritor.newLine();
                    
                }else if( conta_atual instanceof Desenvolvedor){

                    String linha = null;

                    if(conta_atual.getBan()){
                        linha = "BAN_DEV;" + conta_atual.getId() + ";" + conta_atual.getNome() + ";" + conta_atual.getSenha() + ";" + conta_atual.getEmail() + ";" + ((Desenvolvedor) conta_atual).getEmpresa() + ";" + conta_atual.getBan();
                    }else{
                        linha = "DEV;" + conta_atual.getId() + ";" + conta_atual.getNome() + ";" + conta_atual.getSenha() + ";" + conta_atual.getEmail() + ";" + ((Desenvolvedor) conta_atual).getEmpresa() + ";" + conta_atual.getBan();
                    }
                    
                    escritor.write(linha);
                    escritor.newLine();

                }else{

                    String linha = "OP;" + conta_atual.getId() + ";" + conta_atual.getNome() + ";" + conta_atual.getSenha() + ";" + conta_atual.getEmail() + ";" + conta_atual.getBan();
                    escritor.write(linha);
                    escritor.newLine();
                }
            }

            escritor.close();

        } catch (Exception e) {

            System.err.println("Erro de escrita do arquivo contas.txt");
        }
    }

    public static ArrayList<Conta> carregaContas(){

        ArrayList<Conta> contasCarregadas = new ArrayList<>();

        try {

            FileReader arquivo = new FileReader(CAMINHO_CONTAS);
            BufferedReader leitor = new BufferedReader(arquivo);

            String linhaAtual = leitor.readLine();

            while(linhaAtual != null){

                String[] dados = linhaAtual.split(";");

                if(dados[0].equals("USR") || dados[0].equals("BAN_USR")){

                    Usuario contaUsuario = new Usuario(Integer.parseInt(dados[1]), dados[2], Integer.parseInt(dados[3]), dados[4], Double.parseDouble(dados[5]), Boolean.parseBoolean(dados[6]));
                    contasCarregadas.add(contaUsuario);
                }

                else if(dados[0].equals("DEV") || dados[0].equals("BAN_DEV")){

                    Desenvolvedor contaDev = new Desenvolvedor(Integer.parseInt(dados[1]), dados[2], Integer.parseInt(dados[3]), dados[4], dados[5], Boolean.parseBoolean(dados[6]));
                    contasCarregadas.add(contaDev);
                }

                else if(dados[0].equals("OP")){

                    Operador contaOp = new Operador(Integer.parseInt(dados[1]), dados[2], Integer.parseInt(dados[3]), dados[4], Boolean.parseBoolean(dados[5]));
                    contasCarregadas.add(contaOp);
                }

                linhaAtual = leitor.readLine();
            }

            leitor.close();

        } catch (Exception e) {

            System.err.println("Erro de leitura do arquivo contas.txt");
        }
        
        return contasCarregadas;
    }

    public static void salvarRegistro(ArrayList<Registro> listaRegistros){
        
        try {
            
            FileWriter arquivo = new FileWriter(CAMINHO_REGISTROS);
            BufferedWriter escritor = new BufferedWriter(arquivo);

            for(int i = 0; i < listaRegistros.size(); i++){

                Registro registroAtual = listaRegistros.get(i);

                String linha = registroAtual.getId() + ";" + registroAtual.getJogo().getId() + ";" + registroAtual.getConta().getId() + ";" + registroAtual.getHorasJogadas();
                escritor.write(linha);
                escritor.newLine();
            }

            escritor.close();

        } catch (Exception e) {

            System.out.println("Erro de escrita do arquivo registro.txt");
        }
    }

    public static ArrayList<Registro> CarregaRegistros(ArrayList<Jogo> listaJogos, ArrayList<Conta> listaContas){

        ArrayList<Registro> registrosCarregados = new ArrayList<>();

        try {
            
            FileReader arquivo = new FileReader(CAMINHO_REGISTROS);
            BufferedReader leitor = new BufferedReader(arquivo);

            String linhaAtual = leitor.readLine();

            while(linhaAtual != null){

                String[] dados = linhaAtual.split(";");
                Jogo jogoRegistro = null;
                Conta contaRegistro = null;
                
                for(int i = 0; i < listaJogos.size(); i++){

                    Jogo achaJogo = listaJogos.get(i);

                    if(achaJogo.getId() == Integer.parseInt(dados[1])){

                        jogoRegistro = achaJogo;
                        break;
                    }
                }

                if (jogoRegistro == null){

                    linhaAtual = leitor.readLine();
                    continue;
                }

                for(int i = 0; i < listaContas.size(); i++){

                    Conta achaConta = listaContas.get(i);

                    if(achaConta.getId() ==  Integer.parseInt(dados[2])){

                        contaRegistro = achaConta;
                        break;
                    }
                }

                if (contaRegistro == null){

                    linhaAtual = leitor.readLine();
                    continue;
                } 

                Registro registroAtual = new Registro(Integer.parseInt(dados[0]), jogoRegistro, contaRegistro, Double.parseDouble(dados[3]));
                registrosCarregados.add(registroAtual);
                linhaAtual = leitor.readLine();
            }

            leitor.close();

        } catch (Exception e) {

            System.err.println("Erro de leitura do arquivo registros.txt ");
        }

        return registrosCarregados;
    }

    public static boolean existeId(int id, ArrayList<Conta> contas, ArrayList<Jogo> jogos, ArrayList<Registro> registros) {
        for (Conta c : contas) {
            if (c.getId() == id) return true;
        }
        for (Jogo j : jogos) {
            if (j.getId() == id) return true;
        }
        for (Registro r : registros) {
            if (r.getId() == id) return true;
        }
        return false;
}

    public static boolean existeId_contas(int id, ArrayList<Conta> contas) {
        for (Conta c : contas) {
            if (c.getId() == id) return true;
        }
        return false;
    }

    public static boolean existeId_jogos(int id, ArrayList<Jogo> jogos) {
        for (Jogo j : jogos) {
            if (j.getId() == id) return true;
        }
    return false;
}

    public static boolean existeId_registros(int id, ArrayList<Registro> registros) {
        for (Registro r : registros) {
            if (r.getId() == id) return true;
        }
    return false;
    }
}

