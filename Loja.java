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
                int op = scn.nextInt();
                scn.nextLine();
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
                            int sair_loja = scn.nextInt();
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
        }else if(conta instanceof Operador){
            while (true){

                System.out.println("\n============ Loja Bysvem ============");
                System.out.println("\n1 - Jogos Disponíveis\n\n2 - Biblioteca\n\n3 - Configuração\n\n4 - Editar\n\n5 - Sair\n");
                System.out.println("Escolha a opção que deseja.");
                int contador = 1;
                int op = scn.nextInt();
                scn.nextLine();
                switch (op){

                    case 1:
                        jogos_disponiveis(conta);
                        break;

                    case 2:
                        for(Jogo jogo : biblioteca((Usuario) conta)){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        break;    

                    case 3:
                        boolean save = altera_info(conta);
                        //ainda não sei oq vai ser
                        break;

                    case 5:
                        while(true){
                            System.out.println("\nTem certeza que deseja sair?");
                            System.out.println("1) Sim -> Sair\n2) Não -> Voltar");
                            int sair_loja = scn.nextInt();
                            if(sair_loja == 1){return;}
                            else if(sair_loja == 2){break;}
                            else{
                                System.out.println("Escolha apenas 1 ou 2");
                            }
                        }
                        break;

                    case 4:
                        System.out.println("\nDeseja editar:\n1) Usuários      2) Desenvolvedores      3) Jogos");
                        int escolha = scn.nextInt();

                        if(escolha == 1){

                            ArrayList<Usuario> listaUsuarios = criaListaUsuarios();
                            listarUsuario(listaUsuarios);
                            System.out.println("\nDigite 0 para retornar");
                            boolean flag = true;
                            int opcao = -1;
                            
                            while (true) { 
                                System.out.println("\nQual usuário você deseja banir?");
                                try {
                                    opcao = scn.nextInt();
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Opção inválida");
                                    scn.nextLine();
                                }
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
                                        int banir = -1;
                                        while (true) { 
                                            try{
                                                banir = scn.nextInt();
                                                break;
                                            }catch (Exception e) {
                                                System.out.println("Opção inválida.");
                                                scn.nextLine();
                                            }
                                        }

                                        if(banir == 1){
                                            contas.get(i).setBan(false);
                                            Gerenciador.salvarContas(contas);
                                            System.out.println("O usuário(a) " + contas.get(i).getNome() + " foi desbanido(a) com sucesso!");
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
                                        contas.get(i-1).setBan(true);
                                        Gerenciador.salvarContas(contas);
                                        System.out.println("Usuário " + contas.get(i).getNome() + " foi banido(a) com sucesso!");
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


                            while (true) { 
                                System.out.println("\nQual desenvolvedor você deseja banir?");
                                try {
                                    opcao = scn.nextInt();
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Opção inválida");
                                    scn.nextLine();
                                }
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
                                        while (true) { 
                                            try{
                                                banir = scn.nextInt();
                                                break;
                                            }catch (Exception e) {
                                                System.out.println("Opção inválida.");
                                                scn.nextLine();
                                            }
                                        }

                                        if(banir == 1){
                                            contas.get(i).setBan(false);
                                            Gerenciador.salvarContas(contas);
                                            System.out.println("Desenvolvedor(a) " + contas.get(i).getNome() + " foi desbanido(a) com sucesso!");
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
                                        Gerenciador.salvarContas(contas);
                                        System.out.println("Desenvolvedor(a) " + contas.get(i).getNome() + " foi banido(a) com sucesso!");
                                        flag = false;
                                        break;
                                    }
    
                            }if(flag){
                                System.out.println("Nenhum usuário corresponde a opção " + opcao + ".");
                            }


                        }else if(escolha == 3){
                            listarJogos();
                            System.out.println("\nQual jogo você deseja excluir?");
                            int opcao = scn.nextInt();
                            for(int i = 1; i<=jogos.size(); i++){
                                if(i == opcao){
                                    jogos.remove(i);
                                    Gerenciador.salvarJogos(jogos);
                                    jogos = Gerenciador.carregaJogos();
                                }
                            }
                            System.out.println("\nRemoção executada com sucesso!");

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
                int op = scn.nextInt();
                scn.nextLine();
                switch (op){

                    case 1:
                        jogosDev(conta); 
                        break;

                    case 2:    
                        gerenciarJogosDev(conta);
                        break;
                    case 3:
                        boolean save = altera_info(conta);
                        break;

                    case 4:
                        while(true){
                            System.out.println("\nTem certeza que deseja sair?");
                            System.out.println("1) Sim -> Sair\n2) Não -> Voltar");
                            int sair_loja = scn.nextInt();
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

    public void gerenciarJogosDev(Conta conta){
        System.out.println("\n================ Configuração ================");
        System.out.println("1 - Adicionar jogo\n2 - Remover jogo\n3 - Voltar");
        System.out.print("\nSelecione uma opção: ");
        int opc = scn.nextInt();
        while(true){
            if(opc == 1){
                devCriaJogo(conta);
                break;
            }else if(opc == 2){
                boolean removeu = removerJogoDev(conta);
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
                System.out.println("Opção inválida");
            }
        }
    }

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
            int escolha = scn.nextInt();

            if(escolha >= 1 && escolha <= jogosDoDev.size()){
                Jogo escolhido = jogosDoDev.get(escolha - 1);
                imprimirJogo(escolhido);

                System.out.println("\nAperte enter para voltar");
                scn.nextLine();
                scn.nextLine();
                break;

            }else if(escolha == 0){
                break;

            }else{
                System.out.println("Opção Inválida!");
            }
        }
}

    public void devCriaJogo(Conta conta){
        scn.nextLine();
        int id = criaId(2);
        System.out.print("\nInforme o nome do jogo: ");
        String nomeJogo = scn.nextLine();
        System.out.print("Informe o gênero do jogo: ");
        String genero = scn.nextLine();
        System.out.print("Informe o preço do jogo: ");
        double preco = scn.nextDouble();
        scn.nextLine();
        System.out.print("Escreva uma descrição do jogo: ");
        String miniDisc = scn.nextLine();
        Jogo jogo = ((Desenvolvedor)conta).criaJogo(id, nomeJogo, genero, preco, miniDisc);
        jogos.add(jogo);
        Gerenciador.salvarJogos(jogos);
        jogos = Gerenciador.carregaJogos();
        System.out.println("\nSeu jogo foi criado com sucesso!");
        return;
    }

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
            int res = scn.nextInt();

            if(res == 0){
                return false;
            }

            if(res >= 1 && res <= jogosDoDev.size()){
                System.out.println("Tem certeza que deseja remover esse jogo? Ao confirmar não tem como recuperar.");
                System.out.println("Digite 0 se quiser retornar, ou aperte enter para continuar");
                scn.nextLine();
                String remocao = scn.nextLine();
                if(remocao.equals("0")){
                    System.out.println("\nAção cancelada com sucesso!");
                    return false;
                }
                Jogo jogoRemover = jogosDoDev.get(res - 1);

                jogos.remove(jogoRemover);

                Gerenciador.salvarJogos(jogos);
                jogos = Gerenciador.carregaJogos();

                return true;
            } else {
                System.out.println("Número inválido. Tente novamente ou digite 0 para cancelar.");
            }
        }
}

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
            int escolha = scn.nextInt();
            boolean salvar = false;
            int flag = 1;   
            switch(escolha){
                case 1:
                    Perfil_da_Conta(conta);
                    break;
                case 2:
                    String nome = null;
                    while(flag == 1){
                        flag = 0;
                        salvar = true;
                        System.out.print("\nCaso quiser cancelar, digite 0.\nDigite o novo nome: ");
                        nome = scn.next();
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
                        int opcao = scn.nextInt();
                        if(opcao == 1){
                            System.out.println("Seu nome foi alterado com sucesso!");
                            for(int i = 0; i < contas.size(); i++){
                                if(contas.get(i).getId() == conta.getId()){
                                    contas.get(i).setNome(nome);
                                    conta.setNome(nome);
                                }
                            }
                            Gerenciador.salvarContas(contas);
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
                    }
                    break;
                case 3:
                    int senha = 0;
                    while(flag == 1){
                        flag = 0;
                        salvar = true;
                        System.out.print("\nCaso quiser cancelar, digite 0.\nDigite a nova senha: ");
                        senha = scn.nextInt();
                        if(senha == 0){
                            salvar = false;
                            break;
                        }
                        if(senha == (conta.getSenha())){
                            System.out.println("\nA senha deve ser diferente da atual.\n");
                            flag = 1;
                            continue;
                        }
                    }
                    if(!salvar){
                        break;
                    }
                    System.out.println("Nova senha: " + senha + "\nDeseja salvar:\n1 - Salvar\n2 - Cancelar");
                    int opcao = scn.nextInt();
                    if(opcao == 1){
                        System.out.println("Sua senha foi alterada com sucesso!");
                        for(int i = 0; i < contas.size(); i++){
                            if(conta.getId() == contas.get(i).getId()){
                                contas.get(i).setSenha(senha);
                                conta.setSenha(senha);
                            }
                        }
                        Gerenciador.salvarContas(contas);
                        salvar = true;
                        break;
                    }else if(opcao == 0){
                        salvar = false;
                        System.out.println("A senha não foi alterada.");
                        break;
                    }else{
                        System.out.println("Opção inválida.");
                    }
                case 4:
                    flag = 1;
                    String email_novo = null;
                    scn.nextLine(); //Limpar o \n que sobra da resposta
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
                        opcao = scn.nextInt();
                        if(opcao == 1){
                            System.out.println("Seu email foi alterado com sucesso!");
                            alteraEmail(conta, email_novo);
                        }else if(opcao == 2){
                            System.out.println("O email não foi alterado.");
                            break;
                        }else{
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
                        int choose = scn.nextInt();
                        scn.nextLine();
                        if(choose == 1){
                            ApagaUsuario(conta);
                            System.out.print("Escreva o nome da sua empresa: ");
                            String dev_empresa = scn.nextLine();
                            Conta nova_conta = criaDesenvolvedor(conta.getNome(), conta.getSenha(), conta.getEmail(), dev_empresa);
                            System.out.println("\nSua conta foi alterada para desenvolvedor com sucesso!");
                            System.out.println("\nVocê será agora redirecionado ao menu principal e já poderá acessar a nossa loja com a conta de desenvolvedor.\n");
                            conta = nova_conta;
                            alteração_dev = true;
                            condition = false;
                        }
                    } else if(conta instanceof Desenvolvedor){
                        condition = false;
                    }
                    
                    break;
                case 6:
                    condition = false;
                    break;
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
            int escolha = scn.nextInt();
            if(escolha >= 1 && escolha <= jogos.size()){
                Jogo escolhido = jogos.get(escolha - 1);
                boolean voltarLista = false;
                while(!voltarLista){
                    imprimirJogo(escolhido);
                    if(jogoNaBiblioteca(escolhido, biblioteca((Usuario) conta))){
                        System.out.println("[Jogo Adquirido]\nPara voltar, digite 1");
                        int resposta = scn.nextInt();
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
            System.out.println("Saldo: R$" + banker.getSaldo());
        } else if(conta instanceof Desenvolvedor){
            Desenvolvedor companie = (Desenvolvedor) conta;
            System.out.println("Empresa: " + companie.getEmpresa());
        }
        System.out.println("Aperte enter para voltar.");
        scn.nextLine();
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
        int resposta_compra = scn.nextInt();
        if(resposta_compra == 1){
            if(compraJogo(escolhido, (Usuario)conta)){                               
                System.out.println("Jogo adquirido com sucesso!");
                return false;
            }else{
                System.out.println("\nSaldo insuficiente!\n\n1 - Adicionar saldo\n2 - Voltar para os jogos disponíveis");
                int resposta_saldo = scn.nextInt();
                if(resposta_saldo == 1){
                    System.out.print("\nInforme o valor que você deseja adicionar: ");
                    double valor = scn.nextDouble();
                    compraSaldo((Usuario)conta, valor);
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
    
    public boolean compraJogo(Jogo jogoComprado, Usuario usuario){

        if (usuario.getSaldo() >= jogoComprado.getPreco()){

            int id = criaId(1);
            double novoSaldo = usuario.getSaldo() - jogoComprado.getPreco();
            for(int i = 0; i < contas.size(); i++){
                if(contas.get(i).getId() == usuario.getId()){
                    usuario.setSaldo(novoSaldo); 
                    ((Usuario)contas.get(i)).setSaldo(novoSaldo);
                }
            }
            Registro novoRegistro = new Registro(id, jogoComprado, usuario, 0.0);
            this.registros.add(novoRegistro);
            Gerenciador.salvarRegistro(registros);

            return true;
        }

        return false;
    }

    public void compraSaldo(Usuario usuario, double valor) {
        double saldo = usuario.getSaldo();
        System.out.printf("\nSaldo anterior: R$%.2f\nNovo saldo: R$%.2f\n\n", saldo, saldo + valor);
        System.out.println("Deseja confirmar:\n1 - Confirmar\n2 - Voltar");

        while (true) {
            scn.nextLine();
            int opcao;
            try {
                opcao = Integer.parseInt(scn.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite 1 ou 2.");
                continue;
            }

            if (opcao == 1) {
                for (int i = 0; i < this.contas.size(); i++) {
                    if (contas.get(i).getId() == usuario.getId()) {
                        ((Usuario) contas.get(i)).setSaldo(saldo + valor);
                        usuario.setSaldo(saldo + valor);
                        Gerenciador.salvarContas(contas);
                        contas.get(i).setFoisalvo(true);
                        System.out.println("Saldo atualizado com sucesso!");
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

    public ArrayList<Jogo> biblioteca(Usuario usuario){

        ArrayList<Jogo> biblioteca = new ArrayList<>();

        for(int i = 0; i < this.registros.size(); i++){

            if (usuario.getId() == this.registros.get(i).getConta().getId()){

                biblioteca.add(this.registros.get(i).getJogo());
            }
        }

        return biblioteca;
    }

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
        for(Jogo jogo : biblioteca(usuario)){
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

    public void alteraEmail(Conta conta, String novoEmail){

        for(int i = 0; i < this.contas.size(); i++){

            if(contas.get(i).getId() == conta.getId()){        
                contas.get(i).setEmail(novoEmail);
                conta.setEmail(novoEmail);
                Gerenciador.salvarContas(contas);
                contas.get(i).setFoisalvo(true);
                break;
            }
        }
        conta.setFoisalvo(false);
    }

    public void salvaEmail(Conta conta, String novoEmail){

    }

    public Usuario criaUsuario(String nome, int senha, String email){

        int id = criaId(0);
        Usuario novoUsuario = new Usuario(id, nome, senha, email, 0.0, false);
        this.contas.add(novoUsuario);
        Gerenciador.salvarContas(contas);
        novoUsuario.foiSalvo = true;

        return novoUsuario;
    }

    public Desenvolvedor criaDesenvolvedor(String nome, int senha, String email, String empresa){

        int id = criaId(0);
        Desenvolvedor novoDesenvolvedor = new Desenvolvedor(id, nome, senha, email, empresa, false);
        this.contas.add(novoDesenvolvedor);
        Gerenciador.salvarContas(contas);
        novoDesenvolvedor.foiSalvo = true;

        return novoDesenvolvedor;
    }

    public void ApagaUsuario(Conta conta){
        ArrayList<Conta> contas = Gerenciador.carregaContas();

        boolean removido = contas.removeIf(c -> c.getId() == conta.getId());

        if (removido) {
            Gerenciador.salvarContas(contas);
            this.contas = Gerenciador.carregaContas();
        }
    }
}
