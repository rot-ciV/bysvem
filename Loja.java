import java.util.ArrayList;
import java.util.Scanner;

public class Loja extends Bysvem{

    // A loja que centraliza todas as informações: Todos os jogos disponíveis, todas as contas existentes e todos os registros já feitos 
    protected ArrayList<Jogo> jogos;
    protected ArrayList<Conta> contas;
    protected ArrayList<Registro> registros;

    public Loja(int id){
        super(id);
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
                                            if(u.getSaldo() > escolhido.getPreco()){
                                                u.setSaldo(u.getSaldo() - escolhido.getPreco());
                                                u.addJogo(escolhido);
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
}