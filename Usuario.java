
import java.util.ArrayList;
import java.util.Scanner;

public class Usuario extends Conta{
    
    private double saldo;

    public Usuario(int id, String nome, int senha, String email, double saldo, boolean ban){
        
        super(id,nome,senha, email, ban);
        this.saldo = saldo;
    }

    public void setSaldo(double saldo) { this.saldo = saldo; }
    public double getSaldo() { return this.saldo; }

    /*ESSE MÉTODO ERA DE LOJA, PASSEI PARA CÁ E EM LOJA ESTÁ COMENTADO */
    public boolean compraJogo(Jogo jogoComprado, ArrayList<Conta> contas, ArrayList<Registro> registros, Loja loja){

        if (getSaldo() >= jogoComprado.getPreco()){

            
            double novoSaldo = getSaldo() - jogoComprado.getPreco();
            for(int i = 0; i < contas.size(); i++){
                if(contas.get(i).getId() == getId()){
                    setSaldo(novoSaldo); 
                    ((Usuario)contas.get(i)).setSaldo(novoSaldo);
                }
            }
            //MUDEI
            if(atualizar(contas)){
                int id = loja.criaId(1);
                Registro novoRegistro = new Registro(id, jogoComprado, this, 0.0);

                return novoRegistro.salvar(registros);
            }
        }

        return false;
    }

    /*MÉTODO ORIGINALMENTE NA LOJA, FAZ MAIS SENTIDO AQUI*/
    public ArrayList<Jogo> biblioteca(ArrayList<Registro> registros){

        ArrayList<Jogo> biblioteca = new ArrayList<>();

        for(int i = 0; i < registros.size(); i++){

            if (getId() == registros.get(i).getConta().getId()){

                biblioteca.add(registros.get(i).getJogo());
            }
        }

        return biblioteca;
    }

    @Override
    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                super.carregar(id, listaContas);
                Usuario carregado = (Usuario) listaContas.get(i);
                this.saldo = carregado.getSaldo();

                return true;
            }
        }

        return false;
    }

    public void compraSaldo(ArrayList<Conta> contas, Usuario usuario, double valor, Scanner scn) {
        double saldo = usuario.getSaldo();
        System.out.printf("\nSaldo anterior: R$%.2f\nNovo saldo: R$%.2f\n\n", saldo, saldo + valor);
        System.out.println("Deseja confirmar:\n1 - Confirmar\n2 - Voltar");

        while (true) {
           String opcaoString = "";
           int opcao;
           while(true){
                opcaoString = scn.nextLine();
                if(entradaInt(opcaoString)){
                    opcao = Integer.parseInt(opcaoString);
                    break;
                }else{
                    System.out.println("Opção inválida, digite 1 ou 2");
                }
           }

            if (opcao == 1) {
                double saldoAntigo = this.saldo;
                for (int i = 0; i <contas.size(); i++) {
                    if (contas.get(i).getId() == usuario.getId()) {
                        ((Usuario) contas.get(i)).setSaldo(saldo + valor);
                        usuario.setSaldo(saldo + valor);
                        if(usuario.atualizar(contas)){
                            System.out.println("Saldo atualizado com sucesso!");
                        }else{
                            System.out.println("Não foi possível atualizar o saldo.");
                            ((Usuario) contas.get(i)).setSaldo(saldoAntigo);
                            usuario.setSaldo(saldoAntigo);
                        }
                        return;
                    }
                }
            } else if (opcao == 2) {
                System.out.println("Operação cancelada.");
                return;
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    public boolean entradaInt(String valor){
        if(valor != null && valor.matches("-?\\d+")){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String toString(){
        return String.format("\n%s\nSaldo: %.2f", super.toString(), saldo);
    }

}