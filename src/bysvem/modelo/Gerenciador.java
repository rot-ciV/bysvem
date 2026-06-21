package bysvem.modelo;

import bysvem.visao.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public abstract class Gerenciador{

    private static ArrayList<Conta> listaContas = new ArrayList<>();
    private static ArrayList<Jogo> listaJogos = new ArrayList<>();
    private static ArrayList<Registro> listaRegistros = new ArrayList<>();
    private static final String CAMINHO_CONTAS = "dados/contas.txt";
    private static final String CAMINHO_JOGOS = "dados/jogos.txt";
    private static final String CAMINHO_REGISTROS = "dados/registros.txt";
    private static final String CAMINHO_COMPRAS = "dados/compras.txt";
    private Gerenciador() {}

    public static void salvarJogos(ArrayList<Jogo> listaDeJogos){
        garantirDiretorio();
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
        garantirDiretorio();
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
        garantirDiretorio();
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


    public static void carregarRegistros(ArrayList<Jogo> jogos, ArrayList<Conta> contas) {
        listaRegistros = CarregaRegistros(jogos, contas);
    }

    public static void salvarRegistro(Registro registro) {
        listaRegistros.add(registro);
        salvarRegistro(listaRegistros);
    }

    public static void salvarCompras(ArrayList<Compra> compras) {
        garantirDiretorio();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_COMPRAS))) {
            for (Compra compra : compras) {
                for (ItemCompra item : compra.getItens()) {
                    // Formato: idCompra;idUsuario;dataCompra;idItem;idJogo;precoPago;dataValidade
                    String linha = compra.getId() + ";" 
                            + compra.getUsuario().getId() + ";" 
                            + compra.getDataCompra().toString() + ";" 
                            + item.getId() + ";" 
                            + item.getJogo().getId() + ";" 
                            + item.getPrecoPago() + ";" 
                            + item.getDataAtivacao().toString();
                    writer.write(linha);
                    writer.newLine();
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar compras: " + e.getMessage());
        }
    }

    public static void carregarCompras(ArrayList<Conta> contas, ArrayList<Jogo> jogos) {
        // Limpa compras existentes
        for (Conta c : contas) {
            if (c instanceof Usuario) {
                ((Usuario) c).setCompras(new ArrayList<>());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_COMPRAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                // Pular linhas com menos de 7 campos
                if (dados.length < 7) {
                    System.err.println("Linha ignorada (campos insuficientes): " + linha);
                    continue;
                }

                // Para compatibilidade com arquivos antigos que podem ter campos extras,
                // usamos os 7 primeiros campos
                int idCompra = Integer.parseInt(dados[0]);
                int idUsuario = Integer.parseInt(dados[1]);
                LocalDate dataCompra = LocalDate.parse(dados[2]);
                int idItem = Integer.parseInt(dados[3]);
                int idJogo = Integer.parseInt(dados[4]);
                double preco = Double.parseDouble(dados[5]);
                LocalDate dataAtivacao = LocalDate.parse(dados[6]);

                // Localiza usuário
                Usuario usuario = null;
                for (Conta c : contas) {
                    if (c.getId() == idUsuario && c instanceof Usuario) {
                        usuario = (Usuario) c;
                        break;
                    }
                }
                if (usuario == null) {
                    System.err.println("Usuário ID " + idUsuario + " não encontrado.");
                    continue;
                }

                // Localiza jogo
                Jogo jogo = null;
                for (Jogo j : jogos) {
                    if (j.getId() == idJogo) {
                        jogo = j;
                        break;
                    }
                }
                if (jogo == null) {
                    System.err.println("Jogo ID " + idJogo + " não encontrado.");
                    continue;
                }

                // Cria ItemCompra
                ItemCompra item = new ItemCompra(idItem, jogo, preco, dataAtivacao);

                // Verifica se a compra já existe para este usuário
                Compra compraExistente = null;
                for (Compra c : usuario.getCompras()) {
                    if (c.getId() == idCompra) {
                        compraExistente = c;
                        break;
                    }
                }

                if (compraExistente == null) {
                    // Nova compra
                    List<ItemCompra> itens = new ArrayList<>();
                    itens.add(item);
                    compraExistente = new Compra(idCompra, usuario, dataCompra, itens);
                    usuario.adicionarCompra(compraExistente);
                } else {
                    // Adiciona item à compra existente
                    compraExistente.adicionarItem(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo compras.txt não encontrado (primeira execução)");
        } catch (Exception e) {
            System.err.println("Erro ao carregar compras: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void salvarTodasCompras(ArrayList<Conta> contas) {
        garantirDiretorio();
        ArrayList<Compra> todas = new ArrayList<>();
        for (Conta c : contas) {
            if (c instanceof Usuario) {
                todas.addAll(((Usuario) c).getCompras());
            }
        }
        salvarCompras(todas);
    }

    public static void salvarTudo(ArrayList<Jogo> listaDeJogos, ArrayList<Conta> listaContas, ArrayList<Registro> listaRegistros, ArrayList<Conta> contas){
        salvarJogos(listaDeJogos);
        salvarContas(listaContas);
        salvarRegistro(listaRegistros);
        salvarTodasCompras(contas);
    }
    
    public static void removerJogoComReembolso(Jogo jogo) {
        // Carrega contas e jogos
        ArrayList<Conta> contas = carregaContas();
        ArrayList<Jogo> jogos = carregaJogos();
        carregarCompras(contas, jogos); // garante que as compras estão carregadas

        // Percorre todos os usuários
        for (Conta c : contas) {
            if (c instanceof Usuario) {
                Usuario usuario = (Usuario) c;
                List<Compra> compras = usuario.getCompras();
                List<Compra> comprasRemover = new ArrayList<>();
                double valorReembolso = 0.0;

                // Verifica cada compra
                for (Compra compra : compras) {
                    List<ItemCompra> itens = compra.getItens();
                    List<ItemCompra> itensRemover = new ArrayList<>();
                    for (ItemCompra item : itens) {
                        if (item.getJogo().getId() == jogo.getId()) {
                            valorReembolso += item.getPrecoPago();
                            itensRemover.add(item);
                        }
                    }
                    // Remove os itens da compra
                    itens.removeAll(itensRemover);
                    // Se a compra ficou vazia, marca para remover a compra
                    if (itens.isEmpty()) {
                        comprasRemover.add(compra);
                    }
                }

                // Remove compras vazias
                compras.removeAll(comprasRemover);

                // Se houve reembolso, adiciona ao saldo
                if (valorReembolso > 0) {
                    usuario.setSaldo(usuario.getSaldo() + valorReembolso);
                }
            }
        }

        // Agora remove o jogo da lista de jogos
        for (int i = 0; i < jogos.size(); i++) {
            if (jogos.get(i).getId() == jogo.getId()) {
                jogos.remove(i);
                break;
            }
        }

        // Remove os registros associados ao jogo
        ArrayList<Registro> registros = CarregaRegistros(jogos, contas);
        List<Registro> registrosRemover = new ArrayList<>();
        for (Registro r : registros) {
            if (r.getJogo().getId() == jogo.getId()) {
                registrosRemover.add(r);
            }
        }
        registros.removeAll(registrosRemover);

        // Salva tudo
        salvarContas(contas);
        salvarJogos(jogos);
        salvarRegistro(registros);
        salvarTodasCompras(contas);
    }

    private static void garantirDiretorio() {
        File arq = new File("dados");
        if (!arq.exists()) {
            arq.mkdirs();
        }
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

