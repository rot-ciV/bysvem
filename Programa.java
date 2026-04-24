import java.util.ArrayList;
import java.util.Scanner;
public abstract class Programa{
    
    private static Conta buscarConta(String emailProcurado){
        ArrayList<Conta> contas = Gerenciador.carregaContas();

        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            if (c.getEmail().equals(emailProcurado) ) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args){
        Loja loja = new Loja(1);
        loja.jogos = Gerenciador.carregaJogos();
        loja.contas = Gerenciador.carregaContas();
        loja.registros = Gerenciador.CarregaRegistros(loja.jogos, loja.contas);
        Scanner leitor = new Scanner(System.in);
        System.out.println("=============== Olá, seja muito bem vindo ao Bysvem! ===============\n");
        while(true){
            System.out.println("Por favor escolha uma das opções abaixo para continuar");
            System.out.println("1)Entrar na loja        2)Sair");
            int menu_option = leitor.nextInt();
            leitor.nextLine(); //limpar o buffer 
            if(menu_option == 1){
                System.out.println("Para entrar na plataforma você precisa digitar o seu email, ou digite <nova> se quer criar uma conta nova.");    
                String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
                while(true){
                    String email = leitor.nextLine();
                    if(email.matches(regex)){

                        Conta conta_verificadora = buscarConta(email);
                        
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
                            break;//aqui ta saindo da loja e voltando para o menu principal, será que fica melhor sair da loja e fechar o aplicativo?
                        } else{
                            System.out.println("Não foi encontrada uma conta com esse email, tente novamente.");
                            break;
                            }
                    } else if(email.equals("nova")){

                        System.out.println("Você escolheu a opção de criar uma nova conta!");
                        System.out.println("\nCadastre um email: ");
                        String email_novo = leitor.nextLine();
                        
                        while (true) { 

                            if (!email_novo.matches(regex)){
                                System.out.println("Por favor digite um email válido!");
                                email_novo = leitor.nextLine();
                                continue;
                            }

                            if (loja.emailExiste(email_novo)){
                                System.out.println("O email " + email_novo + " já está cadastrado. Tente outro!");
                                email_novo = leitor.nextLine();
                                continue;
                            }

                            break;
                        }
                        
                        System.out.println("\nDigite o nome do usuário: ");
                        String nome = leitor.nextLine();
                        System.out.println("\nCrie uma senha (apenas digitos): ");
                        int senha = leitor.nextInt();

                        loja.criaUsuario(nome, senha, email_novo); 
                        System.out.println("\nConta criada com sucesso!\n");
                        break;

                    } else{
                        System.out.println("Por favor digite um email válido!");
                        }
                }
                
                
            } else if(menu_option == 2){ 
                break;
                } else System.out.println("Infelizmente não temos essa opção.");
            
        }
        leitor.close();
        System.out.println("Obrigado por usar nossa loja, volte sempre!");

    }
}