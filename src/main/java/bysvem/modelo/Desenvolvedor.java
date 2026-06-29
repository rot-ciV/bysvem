package bysvem.modelo;

import java.util.ArrayList;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class Desenvolvedor extends Conta{

    protected String empresa;

    public Desenvolvedor(int id, String nome, int senha, String email, String empresa, boolean ban){
        
        super(id,nome,senha,email, ban);
        this.empresa = empresa;
    }
    
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getEmpresa() { return empresa; }

    public Jogo criaJogo(int id, String nome, String genero, double preco, String disc){

        Jogo novoJogo = new Jogo(id, nome, genero, this.empresa, preco, disc);
        
        return novoJogo; 
    }

    public void removerJogo(Jogo jogo) throws PersistenceException {
        if (!this.empresa.equalsIgnoreCase(jogo.getDesenvolvedora())) {
            throw new PersistenceException("removerJogo", 
                "Este jogo não pertence à sua empresa.", jogo);
        }

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Jogo> jogoDAO = gp.getDAO(Jogo.class);
        EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);
        EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);

        //Reembolsar os compradores
        try {
            Compra[] compras = compraDAO.carregarTodos();
            for (Compra compra : compras) {
                boolean alterou = false;
                for (ItemCompra item : new ArrayList<>(compra.getItens())) {
                    if (item.getJogo().getId() == jogo.getId()) {
                        Usuario u = compra.getUsuario();
                        u.setSaldo(u.getSaldo() + item.getPrecoPago());
                        compra.getItens().remove(item);
                        alterou = true;
                    }
                }
                if (alterou) {
                    compraDAO.atualizar(compra);
                    contaDAO.atualizar(compra.getUsuario());
                }
            }
            if (compras.length > 0) {
                compraDAO.persistir("dados/compras.dat");
                contaDAO.persistir("dados/contas.dat");
            }
        } catch (PersistenceException e) {
            // Nenhuma compra encontrada – prossegue
        }

        jogoDAO.apagar(jogo.getId());
        jogoDAO.persistir("dados/jogos.dat");
    }

    public Jogo adicionarJogo(String nome, String genero, double preco, String desc) throws PersistenceException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do jogo é obrigatório.");
        }
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("O gênero do jogo é obrigatório.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        String descLimpa = (desc != null) ? desc.trim() : "";

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Jogo> jogoDAO = gp.getDAO(Jogo.class);
        int novoId = IdUtil.gerarIdJogo(jogoDAO);

        Jogo novoJogo = this.criaJogo(novoId, nome.trim(), genero.trim(), preco, descLimpa);

        jogoDAO.salvar(novoJogo);
        jogoDAO.persistir("dados/jogos.dat");

        return novoJogo;
    }

    @Override
    public String toString(){
        return String.format("\n%s\nEmpresa: %s", super.toString(), empresa);
    }
}