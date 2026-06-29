package bysvem.modelo;

import bysvem.persistencia.EntidadeDAO;
import bysvem.persistencia.GerenciadorPersistencia;
import bysvem.persistencia.PersistenceException;

public abstract class Conta extends Entidade{

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

    public static Usuario criarConta(String nome, String email, int senha) throws PersistenceException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de usuário é obrigatório.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail é obrigatório.");
        }
        if (!email.trim().contains("@")) {
            throw new IllegalArgumentException("E-mail inválido. Deve conter '@'.");
        }

        String nomeLimpo = nome.trim();
        String emailLimpo = email.trim();

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Conta> dao = gp.getDAO(Conta.class);

        try {
            Conta[] todas = dao.carregarTodos();
            for (Conta c : todas) {
                if (c.getNome().equalsIgnoreCase(nomeLimpo)) {
                    throw new IllegalArgumentException("Usuário já cadastrado!");
                }
                if (c.getEmail().equalsIgnoreCase(emailLimpo)) {
                    throw new IllegalArgumentException("E-mail já está em uso!");
                }
            }
        } catch (PersistenceException e) {
            // Nenhuma conta carregada 
        }
        int novoId = IdUtil.gerarIdConta(dao);

        Usuario novaConta = new Usuario(novoId, nomeLimpo, senha, emailLimpo, 0.0, false);

        dao.salvar(novaConta);
        dao.persistir("dados/contas.dat");

        return novaConta;
    }
    
    public Desenvolvedor promoverParaDesenvolvedor(String empresa) throws PersistenceException {
        if (this instanceof Desenvolvedor) {
            throw new PersistenceException("promoverParaDesenvolvedor", "Conta já é um desenvolvedor.", this);
        }
        if (this instanceof Operador) {
            throw new PersistenceException("promoverParaDesenvolvedor", "Operadores não podem ser promovidos a desenvolvedor.", this);
        }
        Desenvolvedor dev = new Desenvolvedor(this.id, this.nome, this.senha, this.email, empresa, this.ban);
        
        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Conta> dao = gp.getDAO(Conta.class);
        dao.apagar(this.id);
        dao.salvar(dev);
        dao.persistir("dados/contas.dat");
        
        return dev;
    }

    public void atualizarNome(String novoNome) throws PersistenceException {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar vazio.");
        }
        String nomeLimpo = novoNome.trim();
        if (this.nome.equals(nomeLimpo)) {
            throw new IllegalArgumentException("O novo nome deve ser diferente do atual.");
        }

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Conta> dao = gp.getDAO(Conta.class);
        try {
            Conta[] todas = dao.carregarTodos();
            for (Conta c : todas) {
                if (c.getId() != this.id && c.getNome().equalsIgnoreCase(nomeLimpo)) {
                    throw new IllegalArgumentException("Já existe uma conta com este nome.");
                }
            }
        } catch (PersistenceException e) {
            // Nenhuma conta carregada 
        }

        this.nome = nomeLimpo;
        persistirAlteracao(dao);
    }

    public void atualizarSenha(int senhaAtual, int novaSenha) throws PersistenceException {
        if (this.senha != senhaAtual) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        if (novaSenha == senhaAtual) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da atual.");
        }

        this.senha = novaSenha;
        persistirAlteracao(GerenciadorPersistencia.getInstancia().getDAO(Conta.class));
    }

    public void atualizarEmail(String novoEmail) throws PersistenceException {
        if (novoEmail == null || novoEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode estar vazio.");
        }
        String emailLimpo = novoEmail.trim();
        if (!emailLimpo.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido. Deve conter '@'.");
        }
        if (this.email.equals(emailLimpo)) {
            throw new IllegalArgumentException("O novo e-mail deve ser diferente do atual.");
        }

        GerenciadorPersistencia gp = GerenciadorPersistencia.getInstancia();
        EntidadeDAO<Conta> dao = gp.getDAO(Conta.class);
        try {
            Conta[] todas = dao.carregarTodos();
            for (Conta c : todas) {
                if (c.getId() != this.id && c.getEmail().equalsIgnoreCase(emailLimpo)) {
                    throw new IllegalArgumentException("Este e-mail já está em uso.");
                }
            }
        } catch (PersistenceException e) {
            // Nenhuma conta carregada
        }

        this.email = emailLimpo;
        persistirAlteracao(dao);
    }

    private void persistirAlteracao(EntidadeDAO<Conta> dao) throws PersistenceException {
        dao.atualizar(this);
        dao.persistir("dados/contas.dat");
    }

    @Override
    public String toString(){
        String nome =("Nome do perfil: " + getNome());
        String senha = ("Senha: " + getSenha());
        return String.format("\n%s\n%s", nome, senha);
    }
}