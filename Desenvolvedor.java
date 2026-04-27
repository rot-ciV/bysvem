import java.util.ArrayList;

public class Desenvolvedor extends Conta{

    protected String empresa;

    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getEmpresa() { return empresa; }

    public Desenvolvedor(int id, String nome, int senha, String email, String empresa, boolean ban){
        
        super(id,nome,senha,email, ban);
        this.empresa = empresa;
    }

    public Jogo criaJogo(int id, String nome, String genero, double preco, String disc){

        Jogo novoJogo = new Jogo(id, nome, genero, this.empresa, preco, disc);
        
        return novoJogo; 
    }

    @Override
    public boolean carregar(int id, ArrayList<Conta> listaContas){

        for(int i = 0; i < listaContas.size(); i++){

            if(id == listaContas.get(i).getId()){

                super.carregar(id, listaContas);
                Desenvolvedor carregado = (Desenvolvedor) listaContas.get(i);
                this.empresa = carregado.getEmpresa();

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(){
        return String.format("\n%s, Empresa: %s", super.toString(), empresa);
    }

}

 