import java.util.ArrayList;
import java.util.Scanner;
public abstract class Programa{
    
    private static Conta buscarConta(String emailProcurado, ArrayList<Conta> contas){
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
        
        Jogo auxJogo = new Jogo(0, "", "", "", 0.0, "");
        loja.jogos = auxJogo.carregarTodos();
        
        Conta auxConta = new Usuario(0, "", 0, "", 0.0, false);
        loja.contas = auxConta.carregarTodos();

        Registro auxRegistro = new Registro(0, null, null, 0);
        loja.registros = auxRegistro.carregarTodos(loja.contas, loja.jogos);

        Scanner leitor = new Scanner(System.in);
        System.out.println("================ Olá, seja muito bem vindo ao Bysvem! ================\n");
        while(true){
            System.out.println("================ Menu Principal ================\n");
            System.out.println("Por favor escolha uma das opções abaixo para continuar\n");
            System.out.println("1)Entrar na loja        2)Sair\n");
            int menu_option = -1; 

            /*try {
                menu_option = leitor.nextInt();
                leitor.nextLine();

            } catch (Exception e) {
                leitor.nextLine(); //limpar o buffer
            }*/
            String menu_optionString;
            menu_optionString = leitor.nextLine();
            if(loja.entradaInt(menu_optionString)){
                menu_option = Integer.parseInt(menu_optionString);
            }
             
            if(menu_option == 1){
                System.out.println("Para entrar na plataforma você precisa digitar o seu email, ou digite <nova> se quer criar uma conta nova.\n");    
                while(true){
                    System.out.print("Email: ");
                    String email = leitor.nextLine();
                    if(loja.emailValido(email)){

                        Conta conta_verificadora = buscarConta(email, loja.contas);
                        
                        if(conta_verificadora != null){

                            if(conta_verificadora.getBan()){
                                System.out.println("\n <<<<<< AVISO >>>>>>");
                                System.out.println("Sua conta foi banida por violar o termos de uso da Bysvem.\n");
                                break;
                            }
                            boolean flag = true;
                            for(int tent = 3; tent>0; tent--){
                                System.out.print("Digite sua senha: ");
                                //int password = leitor.nextInt();
                                String passwordString;
                                passwordString = leitor.nextLine();
                                if(!loja.entradaInt(passwordString)){
                                    System.out.println("Senha inválida, tente novamente.\n");
                                    continue;
                                }
                                int password = Integer.parseInt(passwordString);       
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

                        System.out.println("\nVocê escolheu a opção de criar uma nova conta!");
                        String email_novo;
                        while(true){
                            System.out.print("Digite seu email: ");
                            email_novo = leitor.nextLine();
                            if(loja.emailExiste(email_novo)){
                                System.out.println("\nO email " + email_novo + " já está em uso.");
                                System.out.println("Por favor, tente outro.\n");
                                continue;

                            }else if(!loja.emailValido(email_novo)){
                                System.out.println("\nEmail inválido! Tente novamente.\n");
                                continue;
                            }

                            break;
                        }
                        System.out.print("Digite o nome do usuário: ");
                        String nome = leitor.nextLine();
                        System.out.print("Crie uma senha (apenas digitos): ");
                        //int senha = leitor.nextInt();
                        int senha;
                        String senhaString;
                        while(true){
                            senhaString = leitor.nextLine();
                            if(loja.entradaInt(senhaString)){
                                senha = Integer.parseInt(senhaString);
                                break;
                            }else{
                                System.out.println("Opção inválida, a senha só pode ter digitos.");
                            }
                        }
                        if(loja.criaUsuario(nome, senha, email_novo)){
                            System.out.println("\nConta criada com sucesso!\n");
                        }else{
                            System.out.println("\nNão foi possível criar a conta.\n");
                        }
                        
                        break;

                    } else{
                        System.out.println("Por favor digite um email válido!");
                        }
                }
                
                
            } else if(menu_option == 2){ 
                break;

            } else System.out.println("Infelizmente não temos essa opção.\n");
            
        }
        leitor.close();
        System.out.println("Obrigado por usar nossa loja, volte sempre!");

    }
}