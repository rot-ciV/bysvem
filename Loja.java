import java.util.ArrayList;

public class Loja extends Bysvem{

    // A loja que centraliza todas as informações: Todos os jogos disponíveis, todas as contas existentes e todos os registros já feitos 
    protected ArrayList<Jogo> jogos;
    protected ArrayList<Conta> contas;
    protected ArrayList<Registro> registros;

    public Loja(int id){
        super(id);
    }

    // public Loja(Usuario user){
    //     //rodar a loja com as informações do usuário sobre o que ele tem (jogos e saldo)
    // }

    // public Loja(Desenvolvedor dev){
    //     //rodar a loja com as informações de dev, dando a ele manuseio e acesso apenas sobre os jogos de sua autoria
    // }

    // public Loja(Operador operator){
    //     //rodar a Loja com as informações de operador, dando a ele manuseio da loja livremente e acesso à todos os jogos
    // }
    
}