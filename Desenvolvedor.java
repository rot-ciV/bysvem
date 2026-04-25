
public class Desenvolvedor extends Conta{

    protected String empresa;

    public void seteEpresa(String empresa) { this.empresa = empresa; }
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
    public String toString(){
        return String.format("\n%s, Empresa: %s", super.toString(), empresa);
    }

}

 