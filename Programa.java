import java.util.Scanner;
import java.util.ArrayList;
public abstract class Programa{
    
    private static Conta buscarConta(int idProcurado){
        ArrayList<Conta> contas = Gerenciador.carregaContas();

        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            if (c.getId() == idProcurado) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args){
        Scanner leitor = new Scanner(System.in);
        System.out.println("=============== Olá, seja muito bem vindo ao Bysvem! ===============\n");
        while(true){
            System.out.println("Por favor escolha uma das opções abaixo para continuar");
            System.out.println("1)Entrar na loja        2)Sair");
            int menu_option = leitor.nextInt();
            if(menu_option == 1){
                System.out.println("Para entrar na plataforma você precisa digitar o seu id, ou digite 0 se quer criar uma conta nova.");
                int user_id = leitor.nextInt();
                if(user_id != 0){
                    Conta conta_verificadora = buscarConta(user_id);
                    Loja loja = new Loja(1);
                    if(conta_verificadora != null){
                        boolean flag = true;
                        for(int tent = 3; tent>0; tent--){
                            System.out.println("Digite sua senha: ");
                            int password = leitor.nextInt();
                            if (password == conta_verificadora.getSenha()){
                                loja.lojaMenu(conta_verificadora);
                                flag = false;
                                break;
                            } else if(tent>1){
                                System.out.println("Senha incorreta, tente novamente.\n");
                            }
                        }
                        if(flag){
                            System.out.println("O limite de tentativas foi excedido, tente novamente mais tarde");
                        }
                    } else{
                        System.out.println("Não foi encontrada uma conta com esse id, tente novamente.");
                        }
                }else {
                    ArrayList<Conta> contas = Gerenciador.salvarContas();


                }
                
            } else if(menu_option == 2){ 
                break;
                } else System.out.println("Infelizmente não temos essa opção.");
            
        }
        leitor.close();
        System.out.println("Obrigado por usar nossa loja, volte sempre!");

    }
}