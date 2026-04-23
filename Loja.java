import java.util.ArrayList;
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
                        System.out.println("Selecione uma opção:\n1 - Alterar nome\n2 - Alterar senha");
                        int escolha = scn.nextInt();
                        
                        if(escolha == 1){
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
                                    }else{
                                        conta.setNome(nome);
                                        Gerenciador.salvarContas(contas);
                                        foiSalvo = true;
                                    }
                                }
                            }else{
                                foiSalvo = false;
                            }
                        }else if(escolha == 2){
                            System.out.println("Digite a nova senha: ");
                            int senha = scn.nextInt();
                            System.out.println("Nova senha: " + senha + "\nDeseja salvar: 1 - Salvar\n2 - Cancelar");
                            int salvar = scn.nextInt();
                            if(salvar == 1){
                                conta.setSenha(senha);
                                foiSalvo = true;
                            }else{
                                foiSalvo = false;
                            }
                        }       
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
                        //ainda não sei oq vai ser            
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
                        //ainda não sei oq vai ser            
                }
            }
        }
        scn.close();
    }

    public int criaId(){

        int id = 2;

        for(int i = 0; i < this.contas.size(); i++){

            if(id <= this.contas.get(i).getId()){

                id = this.contas.get(i).getId() + 1;
            }
        }

        for(int i = 0; i < this.jogos.size(); i++){

            if(id <= this.jogos.get(i).getId()){

                id = this.jogos.get(i).getId() + 1;
            }
        }

        for(int i = 0; i < this.registros.size(); i++){

            if(id <= this.registros.get(i).getId()){

                id = this.registros.get(i).getId() + 1;
            }
        }

        return id;
    }
    //Preciso de uma forma de criar id's
    public boolean compraJogo(Jogo jogoComprado, Usuario usuario){

        if (usuario.getSaldo() >= jogoComprado.getPreco()){

            int id = criaId();
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

        int id = criaId();
        Usuario novoUsuario = new Usuario(id, nome, senha, 0.0);
        this.contas.add(novoUsuario);
        Gerenciador.salvarContas(contas);

        return novoUsuario;
    }

    public Desenvolvedor criaDesenvolvedor(String nome, int senha, String desenvolvedora){

        int id = criaId();
        Desenvolvedor novoDesenvolvedor = new Desenvolvedor(id, nome, senha, desenvolvedora);
        this.contas.add(novoDesenvolvedor);
        Gerenciador.salvarContas(contas);

        return novoDesenvolvedor;
    }

    //public void criOperador(){}
}

