
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Conta extends Entidade<Conta>{

    private String nome;
    private int senha;
    private String email;
    private boolean ban;

    public Conta(int id, String nome, int senha, String email, boolean ban){
        super(id);
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.ban = ban;
    }

    public void setNome(String nome){ this.nome = nome;}
    public void setSenha(int senha){ this.senha = senha;}
    public void setEmail(String email){ this.email = email;}
    public void setBan(boolean ban) { this.ban = ban; }
    
    
    public String getNome(){ return this.nome;}
    public int getSenha(){ return this.senha;}
    public String getEmail(){ return this.email;}
    public boolean getBan() { return this.ban; }

    public boolean salvar(ArrayList<Conta> listaContas){

        if(this.foiSalvo){
            return false;
        }

        listaContas.add(this);
        this.setFoiSalvo(true);
        Gerenciador.salvarContas(listaContas);
        return true;
    }

    public boolean atualizar(ArrayList<Conta> listaContas){

        if(!this.foiSalvo){
            return false;
        }

        Gerenciador.salvarContas(listaContas);
        return true;
    }

    public boolean apagar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                Conta apagada = listaContas.get(i);
                apagada.setFoiSalvo(false);
                listaContas.remove(i);
                Gerenciador.salvarContas(listaContas);
                return true;
            }
        }

        return false;
    }

    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                this.id = listaContas.get(i).getId();
                this.nome = listaContas.get(i).getNome();
                this.senha = listaContas.get(i).getSenha();
                this.email = listaContas.get(i).getEmail();
                this.ban = listaContas.get(i).getBan();
                this.setFoiSalvo(true);

                return true;
            }
        }

        return false;
    }

    public ArrayList<Conta> carregarTodos() {

        ArrayList<Conta> contas = Gerenciador.carregaContas();

        for(Conta c : contas){

            c.setFoiSalvo(true);
        }

        return contas;
    }

    public void alteraNome(Loja loja, Scanner scn, ArrayList<Conta> contas){
        boolean salvar = false;
        int flag = 1; 
        String nome = null;
        while(flag == 1){
            flag = 0;
            salvar = true;
            System.out.print("\nCaso quiser cancelar, digite 0.\nDigite o novo nome: ");
            nome = scn.nextLine();
            if(nome.equalsIgnoreCase("0")){
                salvar = false;
                break;
            }
            if(nome.equalsIgnoreCase(getNome())){
                System.out.println("\nO nome deve ser diferente do nome antigo.\n");
                salvar = false;
                flag = 1;
                continue;
            }
        }
        if(salvar){
            System.out.println("\nNome anterior: " + getNome() + "\nNovo nome: " + nome + "\n\nDESEJA SALVAR?\n" + "1 - Salvar\n0 - Cancelar");
            //int opcao = scn.nextInt();
            int opcao = -1;
            String opcaoString;
            opcaoString = scn.nextLine();
            if(loja.entradaInt(opcaoString)){
                opcao = Integer.parseInt(opcaoString);
            }
            if(opcao == 1){
                
                for(int i = 0; i < contas.size(); i++){
                    if(contas.get(i).getId() == getId()){
                        contas.get(i).setNome(nome);
                        setNome(nome);
                    }
                }
                //MUDEI
                if(atualizar(contas)){
                    System.out.println("Seu nome foi alterado com sucesso!");
                }else{
                    System.out.println("Não foi possível alterar o nome do usuário");
                }

                salvar = true;
                return;
            }else if (opcao == 0){
                salvar = false;
                System.out.println("Nome do usuário não foi alterado.\n");
                return;
            }else{
                System.out.println("Opção inválida.");
                return;
            }
        }
    }

    public void alteraSenha(ArrayList<Conta> contas, Loja loja, Scanner scn){
        boolean salvar = false;
        int flag = 1;
        int senha = -1;
        while(flag == 1){
            flag = 0;
            salvar = true;
            System.out.print("\nCaso quiser cancelar, digite 0.\nDigite a nova senha: ");
            /*try {
                senha = scn.nextInt();
            } catch (Exception e) {
                System.out.println("\nA senha deve ser apenas digitos.\n");
                senha = -1;
                scn.nextLine();
            }*/
            String senhaString;                       
            senhaString = scn.nextLine();
            if(loja.entradaInt(senhaString)){
                senha = Integer.parseInt(senhaString);
            }else{
                System.out.println("A senha deve ser apenas digitos.");
                senha = -1;
            }
            
            if(senha == 0){
                salvar = false;
                break;
            }
            else if(senha == (getSenha())){
                System.out.println("\nA senha deve ser diferente da atual.\n");
                flag = 1;
                continue;
            }
            else if(senha == -1){
                flag = 1;
                continue;
            }
        }
        if(!salvar){
            return;
        }
        System.out.println("Nova senha: " + senha + "\nDeseja salvar:\n1 - Salvar\n2 - Cancelar");
        int opcao = -1;
        String opcaoString;
            opcaoString = scn.nextLine();
        if(loja.entradaInt(opcaoString)){
            opcao = Integer.parseInt(opcaoString);
        }
        if(opcao == 1){
            
            for(int i = 0; i < contas.size(); i++){
                if(getId() == contas.get(i).getId()){
                    contas.get(i).setSenha(senha);
                    setSenha(senha);
                }
            }
            //MUDEI
            if(atualizar(contas)){
                System.out.println("Sua senha foi alterada com sucesso!");
            }else{
                System.out.println("Não foi possível alterar a senha do usuário");
            }

            salvar = true;
            return;
        }else if(opcao == 2){
            salvar = false;
            System.out.println("A senha não foi alterada.");
            return;
        }else{
            System.out.println("Opção inválida.");
            return;
        }
    }

    @Override
    public String toString(){
        String nome =("Nome do perfil: " + getNome());
        String senha = ("Senha: " + getSenha());
        return String.format("\n%s\n%s", nome, senha);
    }
}