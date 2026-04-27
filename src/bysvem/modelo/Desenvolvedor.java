package bysvem.modelo;

import bysvem.visao.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Desenvolvedor extends Conta{

    protected String empresa;

    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getEmpresa() { return empresa; }

    public Desenvolvedor(int id, String nome, int senha, String email, String empresa, boolean ban){
        
        super(id,nome,senha,email, ban);
        this.empresa = empresa;
    }

    public Jogo criaJogo(int id, String nome, String genero, double preco, String disc){

        Jogo novoJogo = new Jogo(id, nome, genero, this.empresa, preco, disc);
        
        return novoJogo; 
    }

    /*MÉTODO ESTAVA NA LOJA, MAS ACHO QUE FICA MELHOR AQUI */
    public void devCriaJogo(ArrayList<Jogo> jogos, Loja loja, Scanner scn){
        int id = loja.criaId(2);
        System.out.print("\nInforme o nome do jogo: ");
        String nomeJogo = scn.nextLine();
        System.out.print("Informe o gênero do jogo: ");
        String genero = scn.nextLine();
        System.out.print("Informe o preço do jogo: ");
        //double preco = scn.nextDouble();
        //scn.nextLine();
        double preco;
        String precoString;
        while(true){
            precoString = scn.nextLine();
            if(loja.entradaDouble(precoString)){
                preco = Double.parseDouble(precoString);
                break;
            }else{
                System.out.println("Opção inválida, digite uma opção válida.");
            }
        }
        System.out.print("Escreva uma descrição do jogo: ");
        String miniDisc = scn.nextLine();
        Jogo jogo = criaJogo(id, nomeJogo, genero, preco, miniDisc);

        //MUDEI
        if(jogo.salvar(jogos)){
            System.out.println("\nSeu jogo foi criado com sucesso!");
        }else{
            System.out.println("\nHouve um erro ao criar o jogo.");
        }
    }

    /*MÉTODO ESTAVA EM LOJA, MAS AQUI FAZ MAIS SENTIDO */
    public void jogosDev(ArrayList<Jogo> jogos, Loja loja, Scanner scn){
        System.out.println("\n================ Jogos Disponíveis ================");

        ArrayList<Jogo> jogosDoDev = new ArrayList<>();

        for (int i = 0; i < jogos.size(); i++) {
            if (jogos.get(i).getDesenvolvedora().equalsIgnoreCase(getEmpresa())) {
                jogosDoDev.add(jogos.get(i));
            }
        }

        if(jogosDoDev.isEmpty()){
            System.out.println("\n_____________ Nenhum jogo disponível _____________");
            return;
        }

        for (int i = 0; i < jogosDoDev.size(); i++) {
            System.out.println((i+1) + " - " + jogosDoDev.get(i).getNome());
            System.out.println("________________________________________");
        }

        while(true){   
            System.out.println("Selecione o jogo que deseja saber mais | (Digite 0 para voltar ao menu): ");
            //int escolha = scn.nextInt();
            int escolha = -1;
            String escolhaString;
            escolhaString = scn.nextLine();
            if(loja.entradaInt(escolhaString)){
                escolha = Integer.parseInt(escolhaString);
            }

            if(escolha >= 1 && escolha <= jogosDoDev.size()){
                Jogo escolhido = jogosDoDev.get(escolha - 1);
                loja.imprimirJogo(escolhido);
                loja.imprimirDadosJogo(escolhido);

                System.out.println("\nAperte enter para voltar");
                //scn.nextLine();
                scn.nextLine();
                break;

            }else if(escolha == 0){
                break;

            }else{
                System.out.println("Opção Inválida!");
            }
        }
    }

    /*MÉTODO ERA DE LOJA, MAS AQUI FAZ MAIS SENTIDO */
        public boolean removerJogoDev(ArrayList<Jogo> jogos, Loja loja, Scanner scn){

            ArrayList<Jogo> jogosDoDev = new ArrayList<>();

            for (Jogo jogo : jogos) {
                if (jogo.getDesenvolvedora().equalsIgnoreCase(getEmpresa())) {
                    jogosDoDev.add(jogo);
                }
            }

            if(jogosDoDev.isEmpty()){
                System.out.println("Você não possui jogos cadastrados.");
                return false;
            }

            for (int i = 0; i < jogosDoDev.size(); i++) {
                System.out.println((i+1) + " - " + jogosDoDev.get(i).getNome());
            }
            System.out.println("Digite o número do jogo que deseja remover, ou 0 para voltar");

            while(true){
                //int res = scn.nextInt();
                int res = -1;
                String resString;
                resString = scn.nextLine();
                if(loja.entradaInt(resString)){
                    res = Integer.parseInt(resString);
                }
                if(res == 0){
                    return false;
                }

                if(res >= 1 && res <= jogosDoDev.size()){
                    System.out.println("Tem certeza que deseja remover esse jogo? Ao confirmar não tem como recuperar.");
                    System.out.println("Digite 0 se quiser retornar, ou aperte enter para continuar");
                    String remocao = scn.nextLine();
                    if(remocao.equals("0")){
                        System.out.println("\nAção cancelada com sucesso!");
                        return false;
                    }
                    
                    return jogos.get(0).apagar(jogosDoDev.get(res - 1).getId(), jogos);
                    
                } else {
                    System.out.println("Número inválido. Tente novamente ou digite 0 para cancelar.");
                }
            }
        }

    public boolean alterarJogosDev(ArrayList<Jogo> jogos, Loja loja, Scanner scn){
    
        ArrayList<Jogo> jogosDoDev = new ArrayList<>();

        for (Jogo jogo : jogos) {
            if (jogo.getDesenvolvedora().equalsIgnoreCase(getEmpresa())) {
                jogosDoDev.add(jogo);
            }
        }

        if(jogosDoDev.isEmpty()){
            System.out.println("Você não possui jogos cadastrados.");
            return false;
        }

        for (int i = 0; i < jogosDoDev.size(); i++) {
            System.out.println((i+1) + " - " + jogosDoDev.get(i).getNome());
        }

        System.out.println("Escolha um jogo para editar (0 para voltar):");

        int res = -1;
        String resString;
        resString = scn.nextLine();
        if(loja.entradaInt(resString)){
            res = Integer.parseInt(resString);
        }

        if(loja.entradaInt(resString)){
            res = Integer.parseInt(resString);
        }

        if(res == 0) return false;

        if(res < 1 || res > jogosDoDev.size()){
            System.out.println("Opção inválida.");
            return false;
        }

        Jogo jogo = jogosDoDev.get(res - 1);
        String new_name = jogo.getNome();
        String new_desc = jogo.getDesc();
        Double new_price = jogo.getPreco();
        while(true){
            System.out.println("\n============= Editando: " + jogo.getNome() + " =============");
            System.out.println("1 - Alterar nome");
            System.out.println("2 - Alterar descrição");
            System.out.println("3 - Alterar preço");
            System.out.println("4 - Salvar e sair");
            System.out.println("5 - Cancelar");

            int op = -1;
            String opString = scn.nextLine();

            if(loja.entradaInt(opString)){
                op = Integer.parseInt(opString);
            }
            

            switch(op){
                case 1:
                    System.out.print("Novo nome: ");
                    new_name = scn.nextLine();
                    break;

                case 2:
                    System.out.print("Nova descrição: ");
                    new_desc = scn.nextLine();
                    break;

                case 3:
                    System.out.print("Novo preço: ");
                    String precoStr = scn.nextLine();
                    if(loja.entradaDouble(precoStr)){
                        new_price = Double.parseDouble(precoStr);
                    }else{
                        System.out.println("Preço inválido.");
                    }
                    break;

                case 4:
                    jogo.setNome(new_name);
                    jogo.setDesc(new_desc);
                    jogo.setPreco(new_price);
                    if(jogo.atualizar(jogos)){
                        System.out.println("Alteração salva com sucesso!");
                        return true;
                    }else{
                        System.out.println("Erro ao atualizar.");
                        return false;
                    }

                case 5:
                    System.out.println("Alteração cancelada.");
                    return false;

                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    public void gerenciarJogosDev(ArrayList<Jogo> jogos, Loja loja, Scanner scn){
        while(true){
            System.out.println("\n================ Configuração ================");
            System.out.println("1 - Adicionar jogo\n2 - Remover jogo\n3 - Alterar Jogo\n4 - Voltar");
            System.out.print("\nSelecione uma opção: ");
            int opc = -1;
            String opcaoString = scn.nextLine();
            if(loja.entradaInt(opcaoString)){
                opc = Integer.parseInt(opcaoString);
            }
            if(opc == 1){
                devCriaJogo(jogos, loja, scn);
                continue;
            }else if(opc == 2){
                boolean removeu = removerJogoDev(jogos, loja, scn);
                if(removeu){
                    System.out.println("Jogo removido com sucesso!");
                    break;
                }else{
                    System.out.println("Nenhum jogo foi removido!");
                }
                continue;
            }else if(opc == 3){
                alterarJogosDev(jogos, loja, scn);
                continue;
            }else if(opc == 4){
                return;
            } else{
                System.out.println("\nOpção inválida\n");
            }
        }
    }

    @Override
    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                super.carregar(id, listaContas);
                Desenvolvedor carregado = (Desenvolvedor) listaContas.get(i);
                this.empresa = carregado.getEmpresa();

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        return String.format("\n%s\nEmpresa: %s", super.toString(), empresa);
    }

}

 