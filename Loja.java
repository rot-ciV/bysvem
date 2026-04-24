import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

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

                System.out.print("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                switch (op){

                    case 4:
                        return;

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
                                    return;
                                }else{
                                    System.out.println("Opção Inválida!");
                                }
                            }

                    case 2:
                        Usuario usuario = (Usuario) conta;
                        for(Jogo jogo : biblioteca(usuario)){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }    

                    case 3:
                        boolean save = altera_info(conta, scn);       
                }
            }
        }else if(conta instanceof Operador){
            while (true){

                System.out.print("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                switch (op){

                    case 4:
                        return;

                    case 1:
                        System.out.println("--- Jogos Disponíveis ---");
                            for (Jogo jogo : jogos) {
                                System.out.println(contador + "-" + jogo.getNome());
                                System.out.println(jogo.getDesc());
                                System.out.println("-------------------");
                                contador++;
                            }

                    case 2:
                        for(Jogo jogo : conta.jogosadquiridos){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }    

                    case 3:
                        boolean save = altera_info(conta, scn);          
                }
            }
        
        }else if(conta instanceof Desenvolvedor){
            while (true){


                System.out.print("\n1 - Jogos Disponíveis\n2 - Biblioteca\n3 - Config\n4 - Sair");
                int contador = 1;
                int op = scn.nextInt();
                switch (op){

                    case 4:
                        return;

                    case 1:
                        System.out.println("--- Jogos Disponíveis ---");
                            for (Jogo jogo : jogos) {
                                System.out.println(contador + "-" + jogo.getNome());
                                System.out.println(jogo.getDesc());
                                System.out.println("-------------------");
                                contador++;
                            }

                    case 2:
                        for(Jogo jogo : conta.jogosadquiridos){
                            System.out.println(contador + "-" + jogo.getNome());
                            System.out.println(jogo.getDesc());
                            System.out.println("-------------------");
                            contador++;
                        }    

                    case 3:
                        boolean save = altera_info(conta, scn);          
                }
            }
        }
        scn.close();
    }

    public boolean altera_info(Conta conta, Scanner scn){
        System.out.println("Selecione uma opção:\n1 - Alterar nome\n2 - Alterar senha\n3 - Alterar o email");
        int escolha = scn.nextInt();
        switch(escolha){
                case 1:
                    System.out.println("Digite o novo nome: ");
                    String nome = scn.next();
                    System.out.println("Nome anterior: " + conta.getNome() + "\nNovo nome: " + nome + "\nDESEJA SALVAR:\n" + 
                    "1 - Salvar\n0 - Cancelar");
                    int salvar = scn.nextInt();
                    if(salvar == 1){
                        for(Conta j : contas){
                            if(j.getNome() == nome){
                                System.out.println("Esse nome de usuário já esta em uso.");
                                foiSalvo = false;
                                break;
                            }
                        }
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
                case 2:
                    System.out.println("Digite a nova senha: ");
                    int senha = scn.nextInt();
                    System.out.println("Nova senha: " + senha + "\nDeseja salvar:\n1 - Salvar\n2 - Cancelar");
                    salvar = scn.nextInt();
                    if(salvar == 1){
                        for(int i = 0; i < contas.size(); i++){
                            if(conta.getId() == contas.get(i).getId()){
                                contas.get(i).setSenha(senha);
                            }
                        }
                        Gerenciador.salvarContas(contas);
                        foiSalvo = true;
                    }else if(salvar == 0){
                        foiSalvo = false;
                        System.out.println("A senha não foi alterada.");
                        break;
                    }else{
                        System.out.println("Opção inválida.");
                    }
                case 3:
                    System.out.println("Digite seu novo email: ");
                    String email_novo = scn.nextLine();
                    for(int i = 0; i < contas.size(); i++){
                        if(contas.get(i).getEmail() == email_novo){
                            System.out.println("O email " + contas.get(i).getId() + " já está cadastrado.");
                            foiSalvo = false;
                            break;
                        }
                    }
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
                    }else if(salvar == 0){
                        System.out.println("O email não foi alterado.");
                        foiSalvo = false;
                        break;
                    }else{
                        System.out.println("Opção inválida");
                    } 
        }
        return foiSalvo;
    }

    public int criaId(int verificação){

        Random sorteador = new Random();
        int id;
        if(verificação == 0){
            //id de usuário
            do{
                id = sorteador.nexInt(9000)+1000;
            }while(Gerenciador.existeId_contas(id, contas));
            return id;
        }
        if(verificação == 1){
            //id de registro
            do{
                id = sorteador.nexInt(900000)+100000;
            }while(Gerenciador.existeId_registros(id, registros));
            return id;
        }
        if(verificação == 2){
            //id de jogo
            do{
                id = sorteador.nexInt(90)+10;
            }while(Gerenciador.existeId_registros(id, jogos));
            return id;
        }
    }
    //Preciso de uma forma de criar id's
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

    public Usuario criaUsuario(String nome, int senha, String email){

        int id = criaId(0);
        Usuario novoUsuario = new Usuario(id, nome, senha, email);
        this.contas.add(novoUsuario);
        Gerenciador.salvarContas(contas);

        return novoUsuario;
    }

    public Desenvolvedor criaDesenvolvedor(String nome, int senha, String email, String desenvolvedora){

        int id = criaId(0);
        Desenvolvedor novoDesenvolvedor = new Desenvolvedor(id, nome, senha, email desenvolvedora);
        this.contas.add(novoDesenvolvedor);
        Gerenciador.salvarContas(contas);

        return novoDesenvolvedor;
    }

    //public void criOperador(){}
}

