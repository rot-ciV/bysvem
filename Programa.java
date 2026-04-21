public abstract class Programa{
    
    private boolean verificador(int idProcurado){
        ArrayList<Conta> contas = Gerenciador.carregaContas();
        boolean existência = false;
        for (int i = 0; i < contas.size(); i++) {
            Conta c = contas.get(i);
            if (c.getId() == idProcurado) {
                existência = true;
                break;
            }
        }
    }

    public static void main(String[] args){
        Scanner leitor = new Scanner(System.in);
        System.out.println("Olá, seja muito bem vindo ao Bysvem!\n");
        while(true){
            System.out.println("Por favor escolha uma das opções abaixo para continuar");
            System.out.println("1)Entrar na loja        2)Sair");
            int menu_option = nexInt();
            if(menu_option == 1){
                System.out.println("Para entrar na plataforma você precisa digitar o seu id:");
                
                int user_id = nexInt();
                ArrayList<Conta> contas = Gerenciador.carregaContas();

                if(id existe){
                    if(id é de dev){
                        //nao sei como vai ser a análise disso, provavvelmente id específicos
                    } else if(user_id == 17 || user_id == 24 || user_id == 67 || user_id == 69){
                        //roda com a conta de um operador 
                    } else{
                        //roda com a conta de um usuário
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