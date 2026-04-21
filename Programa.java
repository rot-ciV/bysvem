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
                System.out.println("Para entrar na plataforma você precisa digitar o seu id:");
                
                int user_id = leitor.nextInt();
                Conta conta_verificadora = buscarConta(user_id);
                Loja loja = new Loja(1);
                if(conta_verificadora != null){
                    Loja.lojaMenu(conta_verificadora);
                //     if(conta_verificadora instanceof Desenvolvedor){
                //         Loja.lojaMenu(conta_verificadora);
                //     } else if(conta_verificadora instanceof Operador){
                //         //roda com a conta de um operador 
                //     } else{
                //         //roda com a conta de um usuário
                //     }
                }
            } else if(menu_option == 2){ 
                break;
                } else System.out.println("Infelizmente não temos essa opção.");
            
        }
        leitor.close();
        System.out.println("Obrigado por usar nossa loja, volte sempre!");

    }
}