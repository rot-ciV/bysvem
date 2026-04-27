import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Loja extends Bysvem{

    // A loja que centraliza todas as informações: Todos os jogos disponíveis, todas as contas existentes e todos os registros já feitos 
    protected ArrayList<Jogo> jogos;
    protected ArrayList<Conta> contas;
    protected ArrayList<Registro> registros;

    public Loja(int id){

        super(id);
        this.jogos = new ArrayList<>();
        this.contas = new ArrayList<>();
        this.registros = new ArrayList<>();
    }

    @Override
    public String toString(){
        return String.format("\n%s, Jogos: %s, Contas: %s, Registros: %s", super.toString(), jogos, contas, registros);
    }

    Scanner scn = new Scanner(System.in);

    public void lojaMenu(Conta conta){
        
        if(conta instanceof Usuario){
            while (true){
                
                System.out.println("\n============ Loja Bysvem ============");
                System.out.println("\n1 - Jogos Disponíveis\n\n2 - Biblioteca\n\n3 - Configuração\n\n4 - Sair\n");
                System.out.println("Escolha a opção que deseja.");
                int contador = 1;
                /*int op = scn.nextInt();
                scn.nextLine();*/
                String opString = "";
                int op;
                opString = scn.nextLine();
                if(entradaInt(opString)){
                    op = Integer.parseInt(opString);
                }else{
                    op = -1;
                }
                switch (op){

                    case 1:
                        jogos_disponiveis(conta);
                        break;

                    case 2:
                        biblioteca_window(contador, conta);
                        break;    

                    case 3:
                        boolean save = altera_info(conta);
                        if(save){
                            return;
                        }
                        break;
                    case 4:
                        while(true){
                            System.out.println("\nTem certeza que deseja sair?");
                            System.out.println("1) Sim -> Sair\n2) Não -> Voltar");
                            //int sair_loja = scn.nextInt();
                            String sairLojaString = "";
                            int sairLoja;
                            sairLojaString = scn.nextLine();
                            if(entradaInt(sairLojaString)){
                                sairLoja = Integer.parseInt(sairLojaString);
                            }else{
                                sairLoja = -1;
                            }
                            if(sairLoja == 1){return;}
                            else if(sairLoja == 2){break;}
                            else{
                                System.out.println("Escolha apenas 1 ou 2");
                            }
                        }
                        break;
                    default:
                        System.out.println("Opção inválida.");       
                }
            }
        }else if(conta instanceof Operador){
            while (true){

                System.out.println("\n============ Loja Bysvem ============");
                System.out.println("\n1 - Jogos Disponíveis\n\n2 - Configuração\n\n3 - Editar\n\n4 - Sair\n");
                System.out.println("Escolha a opção que deseja.");
                int contador = 1;
                /*int op = scn.nextInt();
                scn.nextLine();*/
                String opString = "";
                int op;
                opString = scn.nextLine();
                if(entradaInt(opString)){
                    op = Integer.parseInt(opString);    
                }else{
                    op = -1;
                }
                switch (op){

                    case 1:
                        jogos_disponiveis(conta);
                        break;   

                    case 2:
                        boolean save = altera_info(conta);
                        //ainda não sei oq vai ser
                        break;

                    case 4:
                        while(true){
                            System.out.println("\nTem certeza que deseja sair?");
                            System.out.println("1) Sim -> Sair\n2) Não -> Voltar");
                            //int sair_loja = scn.nextInt();
                            String sair_lojaString = "";
                            int sair_loja;
                            sair_lojaString = scn.nextLine();
                            if(entradaInt(sair_lojaString)){
                                sair_loja = Integer.parseInt(sair_lojaString);
                            }else{
                                sair_loja = -1;
                            }
                            if(sair_loja == 1){return;}
                            else if(sair_loja == 2){break;}
                            else{
                                System.out.println("Escolha apenas 1 ou 2");
                            }
                        }
                        break;

                    case 3:
                        System.out.println("\nDeseja editar:\n0) Voltar      1) Usuários      2) Desenvolvedores      3) Jogos");
                        //int escolha = -1;
                        String escolhaString = "";
                        int escolha;
                        while(true){
                            escolhaString = scn.nextLine();
                            if(entradaInt(escolhaString)){
                                escolha = Integer.parseInt(escolhaString);
                                if(escolha == 0 || escolha == 1 || escolha == 2 || escolha == 3){
                                    break;
                                }
                            }else{
                                System.out.println("Opção inválida, digite uma opção válida.");
                            }
                        }       
                        
                        if(escolha == 1){

                            ArrayList<Usuario> listaUsuarios = criaListaUsuarios();
                            listarUsuario(listaUsuarios);
                            System.out.println("\nDigite 0 para retornar");
                            boolean flag = true;
                            /*int opcao = -1;
                            
                            while (true) { 
                                System.out.println("\nQual usuário você deseja banir?");
                                try {
                                    opcao = scn.nextInt();
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Opção inválida");
                                    scn.nextLine();
                                }
                            }*/
                            int opcao = -1;
                            System.out.println("\nQual usuário você deseja banir?");
                            opString = "";
                            opString = scn.nextLine();
                            if(entradaInt(opString)){
                                opcao = Integer.parseInt(opString);
                            }

                            if(opcao == 0){
                                break;
                            
                            }else if (opcao < 0 || opcao > listaUsuarios.size()) {
                                System.out.println("Nenhum usuário corresponde a essa opção");
                                break;
                            }

                            Conta contaAtual = null;
                            for(int i = 0; i < contas.size(); i++){
                                
                                contaAtual = contas.get(i);
                                if(contaAtual == listaUsuarios.get(opcao - 1)){
                                    if(contas.get(i).getBan()){
                                        System.out.println("O usuário já está banido.");
                                        System.out.println("Gostaria de desbanir " + contas.get(i).getNome()+"?");
                                        System.out.println("1- Sim | 2- Não");
                                        /*int banir = -1;
                                        while (true) { 
                                            try{
                                                banir = scn.nextInt();
                                                break;
                                            }catch (Exception e) {
                                                System.out.println("Opção inválida.");
                                                scn.nextLine();
                                            }
                                        }*/
                                        String banirString = "";
                                        int banir = -1;
                                        banirString = scn.nextLine();
                                        if(entradaInt(banirString)){
                                            banir = Integer.parseInt(banirString);
                                        }

                                        if(banir == 1){
                                            contas.get(i).setBan(false);
                                            //MUDEI
                                            if(contas.get(i).atualizar(contas)){
                                                System.out.println("O usuário(a) " + contas.get(i).getNome() + " foi desbanido(a) com sucesso!");
                                            }
                                            
                                            flag = false;
                                            break;
                                        }else if(banir == 2){
                                            flag = false;
                                            break;
                                        }else{
                                            System.out.println("Opcão inválida.");
                                            flag = false;
                                            break;
                                        }
                                        }
                                        contas.get(i).setBan(true);
                                        //MUDEI
                                        if(contas.get(i).atualizar(contas)){
                                            System.out.println("Usuário " + contas.get(i).getNome() + " foi banido(a) com sucesso!");
                                        }
                                        
                                        flag = false;
                                        break;
                                    }
    
                            }if(flag){
                                System.out.println("Nenhum usuário corresponde a opção " + opcao + ".");
                            }
                            

                        }else if ( escolha == 2) {
                           
                            ArrayList<Desenvolvedor> listaDev = criaListaDev();
                            listarDevs(listaDev);
                            System.out.println("\nDigite 0 para retornar");
                            boolean flag = true;
                            int opcao = -1;

                            /*while (true) { 
                                System.out.println("\nQual desenvolvedor você deseja banir?");
                                try {
                                    opcao = scn.nextInt();
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Opção inválida");
                                    scn.nextLine();
                                }
                            }*/
                            opString = "";
                            opString = scn.nextLine();
                            if(entradaInt(opString)){
                                opcao = Integer.parseInt(opString);
                            }
                            if(opcao == 0){
                                break;
                            
                            }else if (opcao < 0 || opcao > listaDev.size()) {
                                System.out.println("Nenhum desenvolvedor corresponde a essa opção");
                                break;
                            }

                            Conta contaAtual = null;
                            for(int i = 0; i < contas.size(); i++){
                                
                                contaAtual = contas.get(i);
                                if(contaAtual == listaDev.get(opcao - 1)){
                                    if(contas.get(i).getBan()){
                                        System.out.println("O desenvolvedor já está banido.");
                                        System.out.println("Gostaria de desbanir " + contas.get(i).getNome()+"?");
                                        System.out.println("1- Sim | 2- Não");
                                        int banir = -1;
                                        /*while (true) { 
                                            try{
                                                banir = scn.nextInt();
                                                break;
                                            }catch (Exception e) {
                                                System.out.println("Opção inválida.");
                                                scn.nextLine();
                                            }
                                        }*/
                                        String banirString;
                                        banirString = scn.nextLine();
                                        if(entradaInt(banirString)){
                                            banir = Integer.parseInt(banirString);
                                        }

                                        if(banir == 1){
                                            contas.get(i).setBan(false);
                                            //MUDEI
                                            if(contas.get(i).atualizar(contas)){
                                                System.out.println("Desenvolvedor(a) " + contas.get(i).getNome() + " foi desbanido(a) com sucesso!");
                                            }
                                            
                                            flag = false;
                                            break;
                                        }else if(banir == 2){
                                            flag = false;
                                            break;
                                        }else{
                                            System.out.println("Opcão inválida.");
                                            flag = false;
                                            break;
                                        }
                                        }
                                        contas.get(i).setBan(true);
                                        //MUDEI
                                        if(contas.get(i).atualizar(contas)){
                                            System.out.println("Desenvolvedor(a) " + contas.get(i).getNome() + " foi banido(a) com sucesso!");
                                        }
                                        
                                        flag = false;
                                        break;
                                    }
    
                            }if(flag){
                                System.out.println("Nenhum usuário corresponde a opção " + opcao + ".");
                            }


                        }else if(escolha == 3){
                            listarJogos();
                            System.out.println("\nQual jogo você deseja excluir?");
                            //int opcao = scn.nextInt();
                            int opcao;
                            String opcaoString;
                            while(true){
                                opcaoString = scn.nextLine();
                                if(entradaInt(opcaoString)){
                                    opcao = Integer.parseInt(opcaoString);
                                    break;
                                }else{
                                    System.out.println("Opção inválida, digite uma opção válida.");
                                }
                            }
                            //MUDEI
                            if(opcao > 0 && opcao <= jogos.size()){

                                int idAlvo = jogos.get(opcao - 1).getId();

                                if(Jogo.apagar(idAlvo, jogos)){
                                    System.out.println("\nRemoção executada com sucesso!");
                                }else{
                                    System.out.println("\nErro: Não foi possível apagar o jogo");
                                }

                            }else{

                                System.out.println("Opção inválida."); 
                            }
                        }
                        break;

                    default:
                        System.out.println("Opção inválida.");            
                }
            }

        
        }else if(conta instanceof Desenvolvedor){
            while (true){

                System.out.println("\n============ Loja Bysvem ============");
                System.out.println("\n1 - Jogos Disponíveis\n\n2 - Gerenciar jogos\n\n3 - Configuração\n\n4 - Sair\n");
                System.out.println("Escolha a opção que deseja.");
                //int op = scn.nextInt();
                //scn.nextLine();
                int op = -1;
                String opcaoString = scn.nextLine();
                if(entradaInt(opcaoString)){
                    op = Integer.parseInt(opcaoString);
                }
            
                switch (op){

                    case 1:
                        //jogosDev(conta);
                        ((Desenvolvedor)conta).jogosDev(this.jogos, this, scn);  
                        break;

                    case 2:    
                        ((Desenvolvedor)conta).gerenciarJogosDev(this.jogos, this, scn);
                        break;
                    case 3:
                        boolean save = altera_info(conta);
                        break;

                    case 4:
                        while(true){
                            System.out.println("\nTem certeza que deseja sair?");
                            System.out.println("1) Sim -> Sair\n2) Não -> Voltar");
                            //int sair_loja = scn.nextInt();
                            int sair_loja = -1;
                            String sair_lojaString;
                            sair_lojaString = scn.nextLine();
                            if(entradaInt(sair_lojaString)){
                                sair_loja = Integer.parseInt(sair_lojaString);
                            }
                            if(sair_loja == 1){return;}
                            else if(sair_loja == 2){break;}
                            else{
                                System.out.println("Escolha apenas 1 ou 2");
                            }
                        }
                        break;
                    default:
                        System.out.println("Opção inválida.");

                }
            }
        }
        scn.close();
    }

    /*PASSADO PARA DESENVOLVEDOR 
    public void gerenciarJogosDev(Conta conta){
        while(true){
            System.out.println("\n================ Configuração ================");
            System.out.println("1 - Adicionar jogo\n2 - Remover jogo\n3 - Voltar");
            System.out.print("\nSelecione uma opção: ");
            int opc = -1;
            String opcaoString = scn.nextLine();
            if(entradaInt(opcaoString)){
                opc = Integer.parseInt(opcaoString);
            }
            if(opc == 1){
                devCriaJogo(conta);
                break;
            }else if(opc == 2){
                //boolean removeu = removerJogoDev(conta);  ERA USADO QUANDO O MÉTODO ESTAVA NESSA CLASSE LOJA
                boolean removeu = ((Desenvolvedor)conta).removerJogoDev(this.jogos, this, scn);
                if(removeu){
                    System.out.println("Jogo removido com sucesso!");
                    break;
                }else{
                    System.out.println("Nenhum jogo foi removido!");
                }
                break;
            }else if(opc == 3){
                return;
            }else{
                System.out.println("\nOpção inválida\n");
            }
        }
    }*/

    /*PASSADO PARA A CLASSE DESENVOLVEDOR 
    public void jogosDev(Conta conta){
        System.out.println("\n================ Jogos Disponíveis ================");

        ArrayList<Jogo> jogosDoDev = new ArrayList<>();

        for (int i = 0; i < jogos.size(); i++) {
            if (jogos.get(i).getDesenvolvedora().equalsIgnoreCase(((Desenvolvedor) conta).getEmpresa())) {
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
            if(entradaInt(escolhaString)){
                escolha = Integer.parseInt(escolhaString);
            }

            if(escolha >= 1 && escolha <= jogosDoDev.size()){
                Jogo escolhido = jogosDoDev.get(escolha - 1);
                imprimirJogo(escolhido);

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
    }*/
    /*FOI PARA A CLASSE DESENVOLVEDOR 
    public void devCriaJogo(Conta conta){
        int id = criaId(2);
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
            if(entradaDouble(precoString)){
                preco = Double.parseDouble(precoString);
                break;
            }else{
                System.out.println("Opção inválida, digite uma opção válida.");
            }
        }
        System.out.print("Escreva uma descrição do jogo: ");
        String miniDisc = scn.nextLine();
        Jogo jogo = ((Desenvolvedor)conta).criaJogo(id, nomeJogo, genero, preco, miniDisc);

        //MUDEI
        if(jogo.salvar(jogos)){
            System.out.println("\nSeu jogo foi criado com sucesso!");
        }else{
            System.out.println("\nHouve um erro ao criar o jogo.");
        }
    }*/

    /*PASSADO PARA A CLASSE DESENVOLVEDOR
    public boolean removerJogoDev(Conta conta){

        ArrayList<Jogo> jogosDoDev = new ArrayList<>();

        for (Jogo jogo : jogos) {
            if (jogo.getDesenvolvedora().equalsIgnoreCase(((Desenvolvedor) conta).getEmpresa())) {
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
            if(entradaInt(resString)){
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
                
                //MUDEI
                return Jogo.apagar(jogosDoDev.get(res - 1).getId(), jogos);
                
            } else {
                System.out.println("Número inválido. Tente novamente ou digite 0 para cancelar.");
            }
        }
    }*/
    
    public boolean altera_info(Conta conta){
        boolean alteração_dev = false;
        boolean condition = true;
        while(condition){
            System.out.println("\n================ Configuração ================");
            if(conta instanceof Usuario){
                System.out.println("1 - Ver Perfil\n2 - Alterar nome\n3 - Alterar senha\n4 - Alterar o email\n5 - Alterar conta para desenvolvedor\n6 - Voltar\n");
            } else{
                System.out.println("1 - Ver Perfil\n2 - Alterar nome\n3 - Alterar senha\n4 - Alterar o email\n5 - Voltar\n");
            }
            System.out.print("Selecione uma opção: ");
            //int escolha = scn.nextInt();
            int escolha = -1;
            String escolhaString;
            escolhaString = scn.nextLine();
            if(entradaInt(escolhaString)){
                escolha = Integer.parseInt(escolhaString);
            }
            boolean salvar = false;
            int flag = 1;   
            switch(escolha){
                case 1:
                    Perfil_da_Conta(conta);
                    break;
                case 2:
                    /*String nome = null;
                    while(flag == 1){
                        flag = 0;
                        salvar = true;
                        System.out.print("\nCaso quiser cancelar, digite 0.\nDigite o novo nome: ");
                        nome = scn.nextLine();
                        if(nome.equalsIgnoreCase("0")){
                            salvar = false;
                            break;
                        }
                        if(nome.equalsIgnoreCase(conta.getNome())){
                            System.out.println("\nO nome deve ser diferente do nome antigo.\n");
                            salvar = false;
                            flag = 1;
                            continue;
                        }
                    }
                    if(salvar){
                        System.out.println("\nNome anterior: " + conta.getNome() + "\nNovo nome: " + nome + "\n\nDESEJA SALVAR?\n" + "1 - Salvar\n0 - Cancelar");
                        //int opcao = scn.nextInt();
                        int opcao = -1;
                        String opcaoString;
                        opcaoString = scn.nextLine();
                        if(entradaInt(opcaoString)){
                            opcao = Integer.parseInt(opcaoString);
                        }
                        if(opcao == 1){
                            
                            for(int i = 0; i < contas.size(); i++){
                                if(contas.get(i).getId() == conta.getId()){
                                    contas.get(i).setNome(nome);
                                    conta.setNome(nome);
                                }
                            }
                            //MUDEI
                            if(conta.atualizar(contas)){
                                System.out.println("Seu nome foi alterado com sucesso!");
                            }else{
                                System.out.println("Não foi possível alterar o nome do usuário");
                            }

                            salvar = true;
                            break;
                        }else if (opcao == 0){
                            salvar = false;
                            System.out.println("Nome do usuário não foi alterado.\n");
                            break;
                        }else{
                            System.out.println("Opção inválida.");
                            break;
                        }
                    }*/
                    conta.alteraNome(this, scn, this.contas);
                    break;
                case 3:
                    /*int senha = -1;
                    while(flag == 1){
                        flag = 0;
                        salvar = true;
                        System.out.print("\nCaso quiser cancelar, digite 0.\nDigite a nova senha: ");
                        /*try {
                            senha = scn.nextInt();
                        } catch (Exception e) {
                            System.out.println("\nA senha deve ser apenas digitos.\n");
                            senha = -1;
                            scn.nextLine();
                        }*//*
                        String senhaString;                       
                        senhaString = scn.nextLine();
                        if(entradaInt(senhaString)){
                            senha = Integer.parseInt(senhaString);
                        }else{
                            System.out.println("A senha deve ser apenas digitos.");
                            senha = -1;
                        }
                        
                        if(senha == 0){
                            salvar = false;
                            break;
                        }
                        else if(senha == (conta.getSenha())){
                            System.out.println("\nA senha deve ser diferente da atual.\n");
                            flag = 1;
                            continue;
                        }
                        else if(senha == -1){
                            flag = 1;
                            continue;
                        }
                    }
                    if(!salvar){
                        break;
                    }
                    System.out.println("Nova senha: " + senha + "\nDeseja salvar:\n1 - Salvar\n2 - Cancelar");
                    int opcao = -1;
                    /*try {
                        opcao = scn.nextInt();
                    } catch (Exception e) {
                        scn.nextLine();
                        opcao = -1;
                    }*//* 
                    String opcaoString;
                        opcaoString = scn.nextLine();
                    if(entradaInt(opcaoString)){
                        opcao = Integer.parseInt(opcaoString);
                    }
                    if(opcao == 1){
                        
                        for(int i = 0; i < contas.size(); i++){
                            if(conta.getId() == contas.get(i).getId()){
                                contas.get(i).setSenha(senha);
                                conta.setSenha(senha);
                            }
                        }
                        //MUDEI
                        if(conta.atualizar(contas)){
                            System.out.println("Sua senha foi alterada com sucesso!");
                        }else{
                            System.out.println("Não foi possível alterar a senha do usuário");
                        }

                        salvar = true;
                        break;
                    }else if(opcao == 2){
                        salvar = false;
                        System.out.println("A senha não foi alterada.");
                        break;
                    }else{
                        System.out.println("Opção inválida.");
                        break;
                    }*/
                    conta.alteraSenha(contas, this, scn);
                    break;
                case 4:
                    flag = 1;
                    String email_novo = null;
                    //scn.nextLine(); //Limpar o \n que sobra da resposta
                    while(flag == 1){
                        flag = 0;
                        salvar = true;    
                        System.out.print("\nPara voltar, digite 0\nDigite seu novo email: ");
                        email_novo = scn.nextLine();

                        if(email_novo.equalsIgnoreCase("0")){
                            salvar = false;
                            break;
                        }
                        
                        if (emailValido(email_novo)){

                            if(emailExiste(email_novo)){
                                System.out.println("\nO email " + email_novo + " já está cadastrado. Tente outro!\n");
                                flag = 1;
                                continue;
                            }
                            continue;
                        }

                        System.out.println("Por favor, digite um email válido.\n");
                        flag = 1;
                    }
//********************************************************************** */
                    if(salvar){
                        System.out.println("\nEmail anterior: " + conta.getEmail() + "\nNovo email: " + email_novo + "\n");
                        System.out.println("Deseja salvar? \n1 - Salvar\n2 - Cancelar");
                        //opcao = scn.nextInt();
                        int opcao = -1;
                        String opcaoString = "";
                        while(true){
                            opcaoString = scn.nextLine();
                            if(entradaInt(opcaoString)){
                                opcao = Integer.parseInt(opcaoString);
                                if(opcao == 1 || opcao == 2){
                                    break;
                                }else{
                                    System.out.println("Opção inválida, digite 1 ou 2.");
                                }
                            }else{
                                System.out.println("Opção inválida, digite 1 ou 2.");
                            }
                        }
                        if(opcao == 1){
                            
                            if(alteraEmail(conta, email_novo)){
                                System.out.println("Seu email foi alterado com sucesso!");
                            }else{
                                System.out.println("Não foi possível alterar o email.");
                            }
                        }else if(opcao == 2){
                            System.out.println("O email não foi alterado.");
                            break;
                        }else{
                            //ERROOOOO
                            System.out.println("Opção inválida");
                        }
                    }
                    break;
                case 5:
                    if(conta instanceof Usuario){
                        //alteraçao pra conta dev
                        System.out.println("\nVocê escolheu a opçao para alterar para uma conta de desenvolvedor.\n");
                        System.out.println("<<<<<<<<<<< ATENÇÃO >>>>>>>>>>>\n");
                        System.out.println("Ao continuar essa conta deixará de ser uma conta comum e virará de deselvovedora, qualquer jogo que existir na biblioteca será apagado permanentemente");
                        System.out.println("\nAperte 1 para confirmar, ou qualquer outro número para voltar.");
                        /*int choose = scn.nextInt();
                        scn.nextLine();*/
                        int choose;
                        String chooseString;
                        while(true){
                            chooseString = scn.nextLine();
                            if(entradaInt(chooseString)){
                                choose = Integer.parseInt(chooseString);
                                break;
                            }else{
                                System.out.println("Opção inválida, digite 1 para confirmar, ou qualquer outro número para voltar.");
                            }
                        }
                        if(choose == 1){
                            //MUDEI (Também deletei a função apagaUsuario)
                            if(Conta.apagar(conta.getId(), contas)){
                                System.out.print("Escreva o nome da sua empresa: ");
                                String dev_empresa = scn.nextLine();
                                Conta nova_conta = criaDesenvolvedor(conta.getNome(), conta.getSenha(), conta.getEmail(), dev_empresa);
                                System.out.println("\nSua conta foi alterada para desenvolvedor com sucesso!");
                                System.out.println("\nVocê será agora redirecionado ao menu principal e já poderá acessar a nossa loja com a conta de desenvolvedor.\n");
                                conta = nova_conta;
                                alteração_dev = true;
                                condition = false;
                            }else{
                                System.out.println("\nNão foi possível aterar a sua conta para desenvolvedor.");
                                alteração_dev = false;
                                condition = false;
                            }

                        }
                    } else if(conta instanceof Desenvolvedor || conta instanceof Operador){
                        condition = false;
                    }
                    
                    break;
                case 6:
                    if(conta instanceof Usuario){
                        condition = false;
                        break;
                    }

                default:
                    System.out.println("Opção inválida.");
            }
        }
        return alteração_dev;
    }



    public void jogos_disponiveis(Conta conta){
        while(true){   
            listarJogos();
            System.out.println("Selecione o jogo que deseja saber mais | (Digite 0 para voltar ao menu): ");
            //int escolha = scn.nextInt();
            int escolha = -1;
            String escolhaString;
            escolhaString = scn.nextLine();
            if(entradaInt(escolhaString)){
                escolha = Integer.parseInt(escolhaString);
            }
            if(escolha >= 1 && escolha <= jogos.size()){
                Jogo escolhido = jogos.get(escolha - 1);
                boolean voltarLista = false;
                while(!voltarLista){

                    imprimirJogo(escolhido);

                    if(conta instanceof Operador){

                        while (true) { 
                            System.out.println("\nPara voltar, digite 1");
                            int resposta = -1;
                            /*try {
                                resposta = scn.nextInt();
                            }catch (Exception e) {
                                scn.nextLine();
                            }*/
                            String respostaString;
                            respostaString = scn.nextLine();
                            if(entradaInt(respostaString)){
                                resposta = Integer.parseInt(respostaString);
                            }
                            if(resposta == 1){
                                break;
                            }else{
                                System.out.println("Opção inválida");
                            }
                        }
                        
                        voltarLista = true;
                        continue;
                    }
                        
                    
                    if(jogoNaBiblioteca(escolhido, (((Usuario) conta)).biblioteca(this.registros))){
                        System.out.println("[Jogo Adquirido]\nPara voltar, digite 1");
                        //int resposta = scn.nextInt();
                        int resposta = - 1;
                        String respostaString;
                        respostaString = scn.nextLine();
                        if(entradaInt(respostaString)){
                            resposta = Integer.parseInt(respostaString);
                        }
                        if(resposta == 1){
                            voltarLista = true;
                        }else{
                            System.out.println("Opção inválida");
                            continue;
                        }
                    }else{
                        if(conta instanceof Usuario){
                            System.out.println("\nPreço: " + escolhido.getPreco() + "\n");
                            if(conta instanceof Usuario){
                                voltarLista = compradeJogo(conta, escolhido);
                            }
                        }
                    }
                }
            }else if(escolha == 0){
                break;
            }else{
                System.out.println("Opção Inválida!");
                continue;
            }

        }
    }

    public void listarJogos(){
        System.out.println("================ Jogos Disponíveis ================\n");
        for (int i = 1; i <= jogos.size(); i++) {
            System.out.println( i + " - " + jogos.get(i-1).getNome());
            System.out.println("____________________________________\n");
        }
    }

    public ArrayList<Usuario> criaListaUsuarios(){

        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        Conta contaAtual = null;

        for(int i = 0; i < contas.size(); i++){
            contaAtual = contas.get(i);
            if(contaAtual instanceof Usuario){

                listaUsuarios.add((Usuario) contaAtual);
            }
        }

        return listaUsuarios;
    }

    public void listarUsuario(ArrayList<Usuario> listaUsuarios){

        Usuario contaAtual;
        System.out.println("\n--- Usuários Ativos do Bysvem --- ");

        for(int i = 0; i < listaUsuarios.size(); i++){

            contaAtual = listaUsuarios.get(i);
            String ban = "";
            if(contaAtual.getBan()){
                ban = "(Banido)";
            }

            System.out.println(i+1 + "- " + contaAtual.getNome() + " " + ban);
        }
    }

    public ArrayList<Desenvolvedor> criaListaDev(){

        ArrayList<Desenvolvedor> listaDev = new ArrayList<>();
        Conta contaAtual = null;

        for(int i = 0; i < contas.size(); i++){
            contaAtual = contas.get(i);
            if(contaAtual instanceof Desenvolvedor){

                listaDev.add((Desenvolvedor) contaAtual);
            }
        }

        return listaDev;
    }

    public void listarDevs(ArrayList<Desenvolvedor> listaDesenvolvedor){

        Conta contaAtual = null;
        System.out.println("\n--- Desenvolvedores Ativos do Bysvem --- ");

        for(int i = 0; i < listaDesenvolvedor.size(); i++){

            contaAtual = listaDesenvolvedor.get(i);
            String ban = "";
            if(contaAtual.getBan()){
                ban = "(Banido)";
            }
            System.out.println(i+1 + "- " + contaAtual.getNome() + " " + ban);
        }
    }
    
    public void Perfil_da_Conta(Conta conta){
        System.out.println("\n================ Perfil ================");
        System.out.println("Nome do perfil: " + conta.getNome());
        System.out.println("Senha: " + conta.getSenha());
        if(conta instanceof Usuario){
            Usuario banker = (Usuario) conta;
            System.out.printf("Saldo: R$%.2f\n", banker.getSaldo());
        } else if(conta instanceof Desenvolvedor){
            Desenvolvedor companie = (Desenvolvedor) conta;
            System.out.println("Empresa: " + companie.getEmpresa());
        }
        System.out.println("Aperte enter para voltar.");
        //scn.nextLine();
        scn.nextLine();
    }

    public void imprimirJogo(Jogo escolhido){
        System.out.println("================ " + escolhido.getNome() + " ================");
        System.out.println("\nDesenvolvedora: " + escolhido.getDesenvolvedora());
        System.out.println("Gênero: " + escolhido.getGenero());
        System.out.println("\nDescrição: " + escolhido.getDesc());
    }

    public boolean compradeJogo(Conta conta, Jogo escolhido){
        System.out.println("Gostaria de adquirir o jogo?\n1 - Comprar\n2 - Cancelar");
        //int resposta_compra = scn.nextInt();
        int resposta_compra = -1;
        String resposta_compraString;
        resposta_compraString = scn.nextLine();
        if(entradaInt(resposta_compraString)){
            resposta_compra = Integer.parseInt(resposta_compraString);
        }
        if(resposta_compra == 1){
            if(((Usuario)conta).compraJogo(escolhido, contas, registros, this)){                               
                System.out.println("Jogo adquirido com sucesso!");
                return false;
            }else{
                System.out.println("\nSaldo insuficiente!\n\n1 - Adicionar saldo\n2 - Voltar para os jogos disponíveis");
                //int resposta_saldo = scn.nextInt();
                int resposta_saldo;
                String resposta_saldoString;
                while(true){
                    resposta_saldoString = scn.nextLine();
                    if(entradaInt(resposta_saldoString)){
                        resposta_saldo = Integer.parseInt(resposta_saldoString);
                        break;
                    }else{
                        System.out.println("Opção inválida, digite 1 ou 2.");
                    }
                }
                if(resposta_saldo == 1){
                    System.out.print("\nInforme o valor que você deseja adicionar: ");
                    double valorSaldo = 0.0;
                    String valor = "";
                    while(true){
                        valor = scn.nextLine();
                        if(entradaDouble(valor)){
                            valorSaldo = Double.parseDouble(valor);
                            break;
                        }else{
                            System.out.println("Digite um valor válido.");
                        }
                    }
                    compraSaldo((Usuario)conta, valorSaldo);
                    return false;
                }else if(resposta_saldo == 2){
                    return true;
                }else{
                    System.out.println("Opção inválida");
                    return false;
                }
            }
        }else if(resposta_compra == 2){
            return true;
        }else{
            System.out.println("Opção inválida.");
            return false;
        }
        
    }

    public int criaId(int verificação){

        Random sorteador = new Random();
        int id = 0;
        if(verificação == 0){
            //id de contas
            do{
                id = sorteador.nextInt(9000)+1000;
            }while(Gerenciador.existeId_contas(id, contas));
            return id;
        }
        if(verificação == 1){
            //id de registro
            do{
                id = sorteador.nextInt(900000)+100000;
            }while(Gerenciador.existeId_registros(id, registros));
            return id;
        }
        if(verificação == 2){
            //id de jogo
            do{
                id = sorteador.nextInt(90)+10;
            }while(Gerenciador.existeId_jogos(id, jogos));
            return id;
        }
        return id;
    }
    /* MÉTODO FOI PASSADO PARA O USUÁRIO
    public boolean compraJogo(Jogo jogoComprado, Usuario usuario){

        if (usuario.getSaldo() >= jogoComprado.getPreco()){

            
            double novoSaldo = usuario.getSaldo() - jogoComprado.getPreco();
            for(int i = 0; i < contas.size(); i++){
                if(contas.get(i).getId() == usuario.getId()){
                    usuario.setSaldo(novoSaldo); 
                    ((Usuario)contas.get(i)).setSaldo(novoSaldo);
                }
            }
            //MUDEI
            if(usuario.atualizar(contas)){
                int id = criaId(1);
                Registro novoRegistro = new Registro(id, jogoComprado, usuario, 0.0);

                return novoRegistro.salvar(registros);
            }
        }

        return false;
    }*/

    public void compraSaldo(Usuario usuario, double valor) {
        double saldo = usuario.getSaldo();
        System.out.printf("\nSaldo anterior: R$%.2f\nNovo saldo: R$%.2f\n\n", saldo, saldo + valor);
        System.out.println("Deseja confirmar:\n1 - Confirmar\n2 - Voltar");

        while (true) {
            /*scn.nextLine();
            int opcao;
            try {
                opcao = Integer.parseInt(scn.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite 1 ou 2.");
                continue;
            }*/
           String opcaoString = "";
           int opcao;
           while(true){
                opcaoString = scn.nextLine();
                if(entradaInt(opcaoString)){
                    opcao = Integer.parseInt(opcaoString);
                    break;
                }else{
                    System.out.println("Opção inválida, digite 1 ou 2");
                }
           }

            if (opcao == 1) {
                double saldoAntigo = usuario.getSaldo();
                for (int i = 0; i < this.contas.size(); i++) {
                    if (contas.get(i).getId() == usuario.getId()) {
                        ((Usuario) contas.get(i)).setSaldo(saldo + valor);
                        usuario.setSaldo(saldo + valor);
                        //MUDEI
                        if(usuario.atualizar(contas)){
                            System.out.println("Saldo atualizado com sucesso!");
                        }else{
                            System.out.println("Não foi possível atualizar o saldo.");
                            ((Usuario) contas.get(i)).setSaldo(saldoAntigo);
                            usuario.setSaldo(saldoAntigo);
                        }
                        return;
                    }
                }
            } else if (opcao == 2) {
                System.out.println("Operação cancelada.");
                return;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
    
    /*PASSADO PARA USUÁRIO 
    public ArrayList<Jogo> biblioteca(Usuario usuario){

        ArrayList<Jogo> biblioteca = new ArrayList<>();

        for(int i = 0; i < this.registros.size(); i++){

            if (usuario.getId() == this.registros.get(i).getConta().getId()){

                biblioteca.add(this.registros.get(i).getJogo());
            }
        }

        return biblioteca;
    }*/

    public boolean jogoNaBiblioteca(Jogo jogo, ArrayList<Jogo> biblioteca){

        for(int i = 0; i < biblioteca.size(); i++){

            if(jogo.getId() == biblioteca.get(i).getId()){
                return true;
            }
        }
        return false;
    }

    public void biblioteca_window(int contador, Conta conta){

        System.out.println("\n============ Sua Biblioteca ============\n");
        Usuario usuario = (Usuario) conta;
        for(Jogo jogo : (usuario).biblioteca(this.registros)){
            System.out.println(contador + "-" + jogo.getNome());
            System.out.println(jogo.getDesc());
            System.out.println("-------------------------------------------------");
            contador++;
        }
        System.out.println("Aperte enter para voltar a loja.");
        scn.nextLine();
    }

    public boolean emailValido(String email){
        
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

    public boolean emailExiste(String email){
            
        for(int i = 0; i < contas.size(); i++){
            if(contas.get(i).getEmail().equalsIgnoreCase(email)){
                return true;
            }    
        }
        return false;
    }

    public boolean alteraEmail(Conta conta, String novoEmail){

        for(int i = 0; i < this.contas.size(); i++){

            if(contas.get(i).getId() == conta.getId()){        
                contas.get(i).setEmail(novoEmail);
                conta.setEmail(novoEmail);
                //MUDEI
                return conta.atualizar(contas);
            }
        }
        return false;
    }

    public boolean criaUsuario(String nome, int senha, String email){

        int id = criaId(0);
        Usuario novoUsuario = new Usuario(id, nome, senha, email, 0.0, false);
        //MUDEI
        return novoUsuario.salvar(contas);
    }

    public Desenvolvedor criaDesenvolvedor(String nome, int senha, String email, String empresa){

        int id = criaId(0);
        Desenvolvedor novoDesenvolvedor = new Desenvolvedor(id, nome, senha, email, empresa, false);
        //MUDEI
        novoDesenvolvedor.salvar(contas);

        return novoDesenvolvedor;
    }

    public boolean entradaDouble(String valor){
        if(valor != null && valor.matches("-?\\d+(\\.\\d+)?")){
            return true;
        }else{
            return false;
        }
    }

    public boolean entradaInt(String valor){
        if(valor != null && valor.matches("-?\\d+")){
            return true;
        }else{
            return false;
        }
    }
}
