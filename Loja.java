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

    public void lojaMenu(Conta conta){
        Scanner scn = new Scanner(System.in);
        
        if(conta instanceof Usuario){
            while (true){

                System.out.println("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                scn.nextLine();
                switch (op){

                    case 1:
                        System.out.println("--- Jogos Disponíveis ---");
                        for (Jogo jogo : jogos) {
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        while (true){
                            System.out.println("Selecione o jogo que deseja saber mais | (Digite 0 para voltar ao menu): ");
                            int escolha = scn.nextInt();
                            if(escolha >= 1 && escolha <= jogos.size()){
                                Jogo escolhido = jogos.get(escolha - 1);
                                if(conta.PossuiJogo(escolhido) == true){
                                    System.out.println("Jogo selecionado: " + escolhido.getNome());
                                    System.out.println("Descrição: " + escolhido.getDesc());
                                    System.out.println("Gênero: " + escolhido.getGenero());
                                    System.out.println("Desenvolvedora: " + escolhido.getDesenvolvedora());
                                    System.out.println("[Jogo Adquirido]");
                                }else{
                                    System.out.println("Jogo selecionado: " + escolhido.getNome());
                                    System.out.println("Descrição: " + escolhido.getDesc());
                                    System.out.println("Gênero: " + escolhido.getGenero());
                                    System.out.println("Desenvolvedora: " + escolhido.getDesenvolvedora());
                                    System.out.println("Preço: " + escolhido.getPreco());
                                    System.out.println("Gostaria de adquirir o jogo? (y/n)");
                                    String resposta = scn.next();
                                    if(resposta.equals('y')){
                                        Usuario u = (Usuario) conta;
                                        if(compraJogo(escolhido, u)){
                                            
                                            System.out.println("Jogo adquirido com sucesso!");
                                        }else{
                                            System.out.println("Saldo insuficiente!");
                                            break;
                                        }
                                    }else{
                                        break;
                                    }
                                }

                            }else if(escolha == 0){
                                break;
                            }else{
                                System.out.println("Opção Inválida!");
                            }
                        }
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
                        boolean save = altera_info(conta, scn);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida.");       
                }
            }
        }else if(conta instanceof Operador){
            while (true){

                System.out.println("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                scn.nextLine();
                switch (op){

                    case 1:
                        System.out.println("--- Jogos Disponíveis ---");
                        for (Jogo jogo : jogos) {
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        break;

                    case 2:
                        for(Jogo jogo : conta.jogosadquiridos){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        break;    

                    case 3:
                        boolean save = altera_info(conta, scn);
                        //ainda não sei oq vai ser
                        break;

                    case 4:
                        return;
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
                        System.out.println("--- Jogos Disponíveis ---");
                            for (Jogo jogo : jogos) {
                                System.out.println(contador + "-" + jogo.getNome());
                                System.out.println(jogo.getDesc());
                                System.out.println("-------------------");
                                contador++;
                            }
                        break;

                    case 2:
                        for(Jogo jogo : conta.jogosadquiridos){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }
                        break;    

                    case 3:
                        boolean save = altera_info(conta, scn);

                    case 4:
                        return;
                    default:
                        System.out.println("Opção inválida.");

                }
            }
        }
        scn.close();
    }

    public boolean altera_info(Conta conta, Scanner scn){
        System.out.println("Selecione uma opção:\n1 - Alterar nome\n2 - Alterar senha\n3 - Alterar o email\n4 - Voltar");
        int escolha = scn.nextInt();
        switch(escolha){
                case 1:
                    int flag = 1;
                    String nome = null;
                    while(flag == 1){
                        flag = 0;
                        foiSalvo = true;
                        System.out.println("Caso quiser cancelar, digite 0\nDigite o novo nome: ");
                        nome = scn.next();
                        if(nome.equalsIgnoreCase("0")){
                            foiSalvo = false;
                            break;
                        }
                        for(Conta j : contas){
                            if(j.getNome().equalsIgnoreCase(nome)){
                                System.out.println("Esse nome de usuário já esta em uso. Tente outro");
                                foiSalvo = false;
                                flag = 1;
                                break;
                            }
                        }
                    }
                    if(foiSalvo){
                        System.out.println("Nome anterior: " + conta.getNome() + "\nNovo nome: " + nome + "\nDESEJA SALVAR:\n" + 
                        "1 - Salvar\n0 - Cancelar");
                        int salvar = scn.nextInt();
                        if(salvar == 1){
                            for(int i = 0; i < contas.size(); i++){
                                if(contas.get(i).getId() == conta.getId()){
                                    contas.get(i).setNome(nome);
                                }
                            }
                            Gerenciador.salvarContas(contas);
                            foiSalvo = true;
                            break;
                        }else if (salvar == 0){
                            foiSalvo = false;
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
                    int salvar = scn.nextInt();
                    if(salvar == 1){
                        for(int i = 0; i < contas.size(); i++){
                            if(conta.getId() == contas.get(i).getId()){
                                contas.get(i).setSenha(senha);
                            }
                        }
                        Gerenciador.salvarContas(contas);
                        foiSalvo = true;
                        break;
                    }else if(salvar == 0){
                        foiSalvo = false;
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
                        foiSalvo = true;    
                        System.out.println("Para voltar, digite 0\nDigite seu novo email: ");
                        email_novo = scn.nextLine();
                        if(email_novo.equalsIgnoreCase("0")){
                            foiSalvo = false;
                            break;
                        }
                        
                        if(emailExiste(email_novo)){
                            System.out.println("O email " + email_novo + " já está cadastrado. Tente outro!");
                            foiSalvo = false;
                            flag = 1;
                            break;
                        }   
                        
                    }
                    if(foiSalvo){
                        System.out.println("Email anterior: " + conta.getEmail() + "\nNovo email: " + email_novo +
                            "\nDeseja salvar \n1 - Salvar\n2 - Cancelar");
                        salvar = scn.nextInt();
                        if(salvar == 1){
                            for(int i = 0; i < contas.size(); i++){
                                if(conta.getId() == contas.get(i).getId()){
                                    conta.setEmail(email_novo);
                                    contas.get(i).setEmail(email_novo);
                                }
                            }
                            Gerenciador.salvarContas(contas);
                            foiSalvo = true;
                            break;
                        }else if(salvar == 2){
                            System.out.println("O email não foi alterado.");
                            foiSalvo = false;
                            break;
                        }else{
                            System.out.println("Opção inválida");
                        }
                    }
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opção inválida.");
        }
        return foiSalvo;
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
            usuario.setSaldo(novoSaldo); 

            Registro novoRegistro = new Registro(id, jogoComprado, usuario, 0.0);
            this.registros.add(novoRegistro);
            Gerenciador.salvarRegistro(registros);

            return true;
        }

        return false;
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

    public boolean emailExiste(String email){

        for(int i = 0; i < contas.size(); i++){
            
            if(contas.get(i).getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        
        return false;
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

}

