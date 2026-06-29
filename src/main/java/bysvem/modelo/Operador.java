package bysvem.modelo;

import java.util.ArrayList;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public class Operador extends Conta{
    
    
    public Operador(int id, String nome, int senha, String email, boolean ban){
        super(id,nome,senha,email, ban);
    }



    public void banConta(Conta conta){
        conta.setBan(true);
    }

    public static void removerJogo(Jogo jogo) throws PersistenceException {
        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Jogo> jogoDAO = gp.getDAO(Jogo.class);
        EntidadeDAO<Conta> contaDAO = gp.getDAO(Conta.class);
        EntidadeDAO<Compra> compraDAO = gp.getDAO(Compra.class);

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
            // Sem compras
        }

        jogoDAO.apagar(jogo.getId());
        jogoDAO.persistir("dados/jogos.dat");
    }

    @Override
    public String toString(){
        return String.format("\n%s", super.toString());
    }
}