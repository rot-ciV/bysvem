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

    Scanner scn = new Scanner(System.in);

    public void lojaMenu(Conta conta){
        
        if(conta instanceof Usuario){
            while (true){

                System.out.println("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                scn.nextLine();
                switch (op){

                    case 1:
                        jogos_disponiveis(conta);
                        break;

                    case 2:
                        Usuario usuario = (Usuario) conta;
                        for(Jogo jogo : biblioteca(usuario)){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        break;    

                    case 3:
                        boolean save = altera_info(conta);
                        if(save){ //mudanca possiveis
                            return;
                        }
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida.");       
                }
            }
        }else if(conta instanceof Operador){
            while (true){

                System.out.println("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Editar\n5 - Sair");
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
                        return;

                    case 4:
                        System.out.println("\nDeseja editar:\n1 - Usuários      2 - Jogos\n");
                        int escolha = scn.nextInt();
                        if(escolha == 1){
                            //falta pensar oq fazer
                        }else if(escolha == 2){
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


                System.out.println("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
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
                        break;

                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida.");

                }
            }
        }
        scn.close();
    }

    public boolean altera_info(Conta conta){
        System.out.println("Selecione uma opção:");
        boolean alteração_dev = false;
        boolean condition = true;
        while(condition){
            if(conta instanceof Usuario){
                System.out.println("1 - Alterar nome\n2 - Alterar senha\n3 - Alterar o email\n4 - Alterar conta para desenvolvedor\n5 - Voltar");
            } else{
                System.out.println("1 - Alterar nome\n2 - Alterar senha\n3 - Alterar o email\n4 - Voltar");
            }
            int escolha = scn.nextInt();
            boolean salvar = false;
            switch(escolha){
                case 1:
                    int flag = 1;
                    String nome = null;
                    while(flag == 1){
                        flag = 0;
                        salvar = true;
                        System.out.println("Caso quiser cancelar, digite 0\nDigite o novo nome: ");
                        nome = scn.next();
                        if(nome.equalsIgnoreCase("0")){
                            salvar = false;
                            break;
                        }
                        if(nome.equalsIgnoreCase(conta.getNome())){
                            System.out.println("O nome deve ser diferente do nome antigo.\n");
                            salvar = false;
                            flag = 1;
                            continue;
                        }
                    }
                    if(salvar){
                        System.out.println("Nome anterior: " + conta.getNome() + "\nNovo nome: " + nome + "\nDESEJA SALVAR:\n" + 
                        "1 - Salvar\n0 - Cancelar");
                        int opcao = scn.nextInt();
                        if(opcao == 1){
                            System.out.println("Seu novo nome foi alterado com sucesso!");
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
                            System.out.println("Nome do usuário não foi alterado.");
                            break;
                        }else{
                            System.out.println("Opção inválida.");
                            break;
                        }
                    }
                    break;
                case 2:
                    System.out.println("Digite a nova senha: ");
                    int senha = scn.nextInt();
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
                case 3:
                    flag = 1;
                    String email_novo = null;
                    scn.nextLine(); //Limpar o \n que sobra da resposta
                    while(flag == 1){
                        flag = 0;
                        salvar = true;    
                        System.out.println("Para voltar, digite 0\nDigite seu novo email: ");
                        email_novo = scn.nextLine();

                        if(email_novo.equalsIgnoreCase("0")){
                            salvar = false;
                            break;
                        }
                        
                        if (emailValido(email_novo)){

                            if(emailExiste(email_novo)){
                                System.out.println("O email " + email_novo + " já está cadastrado. Tente outro!\n");
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
                        System.out.println("Email anterior: " + conta.getEmail() + " | Novo email: " + email_novo);
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
                case 4:
                    if(conta instanceof Usuario){
                        //alteraçao pra conta dev
                        System.out.println("Você escolheu a opçao para alterar para uma conta de desenvolvedor.");
                        System.out.println("<<<<<< ATENÇÃO >>>>>>");
                        System.out.println("Ao continuar essa conta deixará de ser uma conta comum e virará de deselvovedora, qualquer jogo que existir na biblioteca será apagado permanentemente");
                        System.out.println("Aperte 1 para confirmar, ou qualquer outro número para voltar.");
                        int choose = scn.nextInt();
                        scn.nextLine();
                        if(choose == 1){
                            ApagaUsuario(conta);
                            System.out.println("Qual o nome da sua empresa?");
                            String dev_empresa = scn.nextLine();
                            Conta nova_conta = criaDesenvolvedor(conta.getNome(), conta.getSenha(), conta.getEmail(), dev_empresa);
                            System.out.println("Sua conta foi alterada para desenvolvedor com sucesso!");
                            System.out.println("Você será agora redirecionado ao menu principal e já poderá acessar a nossa loja com a conta de desenvolvedor.");
                            conta = nova_conta;
                        }
                        alteração_dev = true;
                    }
                    condition = false;
                    break;
                case 5:
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
                        System.out.println("Preço: " + escolhido.getPreco());
                        if(conta instanceof Usuario){
                            voltarLista = compradeJogo(conta, escolhido);
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
        System.out.println("--- Jogos Disponíveis ---");
        for (int i = 1; i <= jogos.size(); i++) {
            System.out.println( i + "-" + jogos.get(i-1).getNome());
            System.out.println("-------------------");
        }
    }

    public void imprimirJogo(Jogo escolhido){
        System.out.println("Jogo selecionado: " + escolhido.getNome());
        System.out.println("Descrição: " + escolhido.getDesc());
        System.out.println("Gênero: " + escolhido.getGenero());
        System.out.println("Desenvolvedora: " + escolhido.getDesenvolvedora());
    }

    public boolean compradeJogo(Conta conta, Jogo escolhido){
        System.out.println("Gostaria de adquirir o jogo?\n1 - Comprar\n2 - Cancelar");
        int resposta_compra = scn.nextInt();
        if(resposta_compra == 1){
            if(compraJogo(escolhido, (Usuario)conta)){                               
                System.out.println("Jogo adquirido com sucesso!");
                return false;
            }else{
                System.out.println("Saldo insuficiente!\n1 - Adicionar saldo\n2 - Voltar para os jogos disponíveis");
                int resposta_saldo = scn.nextInt();
                if(resposta_saldo == 1){
                    System.out.println("Informe o valor que você deseja adicionar");
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

    public void compraSaldo(Usuario usuario, double valor){
        double saldo = usuario.getSaldo();
        System.out.println("Saldo anterior: R$" + saldo + "\nNovo saldo: R$" + (saldo + valor) + "\nDeseja confirmar:\n1 - Confirmar\n2 - Voltar");
        while(true){
            if(scn.nextInt() == 1){
                for(int i = 0; i < this.contas.size(); i++){
                    if(contas.get(i).getId() == usuario.getId()){
                        ((Usuario)contas.get(i)).setSaldo(saldo + valor);
                        usuario.setSaldo(saldo + valor);
                        Gerenciador.salvarContas(contas);
                        contas.get(i).setFoisalvo(true);
                        return;
                    }
                }
            }else if(scn.nextInt() == 2){
                return;
            }else{
                System.out.println("Opção inválida. Tente novamente");
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
        Usuario novoUsuario = new Usuario(id, nome, senha, email, 0.0);
        this.contas.add(novoUsuario);
        Gerenciador.salvarContas(contas);
        novoUsuario.foiSalvo = true;

        return novoUsuario;
    }

    public Desenvolvedor criaDesenvolvedor(String nome, int senha, String email, String empresa){

        int id = criaId(0);
        Desenvolvedor novoDesenvolvedor = new Desenvolvedor(id, nome, senha, email, empresa);
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
