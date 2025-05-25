package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.dao.PessoaDAO;
import br.com.fiap.fintech.dao.UsuarioDAO;
import br.com.fiap.fintech.factory.DaoFactory;
import br.com.fiap.fintech.model.Conta;
import br.com.fiap.fintech.model.Pessoa;
import br.com.fiap.fintech.model.Usuario;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class CadastroService {

    private PessoaDAO pessoaDAO;
    private UsuarioDAO usuarioDAO;
    private ContaDAO contaDAO;

    public CadastroService() throws SQLException {
        // Usando DaoFactory para obter as instâncias dos DAOs
        this.pessoaDAO = DaoFactory.getPessoaDAO();
        this.usuarioDAO = DaoFactory.getUsuarioDAO();
        this.contaDAO = DaoFactory.getContaDAO();
    }

    /**
     * Executa o processo de cadastro completo de um novo usuário.
     *
     * @param nome Nome completo da pessoa.
     * @param cpf CPF da pessoa (sem formatação ou já formatado, dependendo da sua validação).
     * @param dataNascimento Data de nascimento da pessoa.
     * @param email Email do usuário para login.
     * @param senha Senha do usuário.
     * @return O objeto Usuario criado e persistido.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws IllegalArgumentException Se dados inválidos forem fornecidos (ex: CPF/Email já existe).
     */
    public Usuario cadastrarNovoUsuario(String nome, String cpf, LocalDate dataNascimento, String email, String senha)
            throws SQLException, IllegalArgumentException {

        // 1. Validações prévias (exemplo: verificar se CPF/Email já existem)
        if (pessoaDAO.buscarPorCpf(cpf) != null) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
        if (usuarioDAO.buscarPorEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        // 2. Criar e inserir a Pessoa
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(nome);
        novaPessoa.setCpf(cpf);
        novaPessoa.setDataNascimento(dataNascimento);
        pessoaDAO.inserir(novaPessoa); // O ID será preenchido pelo DAO

        if (novaPessoa.getCodigo() == null) {
            throw new SQLException("Falha ao obter o código da pessoa após inserção.");
        }

        // 3. Criar e inserir o Usuario
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha); // Em um sistema real, use HASHING para a senha!
        novoUsuario.setAtivo(true);
        novoUsuario.setCodigoPessoa(novaPessoa.getCodigo());
        usuarioDAO.inserir(novoUsuario); // O ID será preenchido pelo DAO

        if (novoUsuario.getCodigo() == null) {
            // Idealmente, aqui deveria ter um rollback da inserção da Pessoa
            throw new SQLException("Falha ao obter o código do usuário após inserção.");
        }

        // 4. Criar uma conta padrão (ex: Carteira) para o novo usuário
        Conta contaPadrao = new Conta();
        contaPadrao.setNome("Carteira Principal");
        contaPadrao.setTipo("CARTEIRA"); // Use as constantes definidas no CHECK do BD
        contaPadrao.setSaldo(BigDecimal.ZERO);
        contaPadrao.setCodigoUsuario(novoUsuario.getCodigo());
        contaDAO.inserir(contaPadrao);

        System.out.println("Usuário cadastrado com sucesso: " + novoUsuario.getEmail());
        System.out.println("Pessoa associada: " + novaPessoa.getNome());
        System.out.println("Conta padrão criada: " + contaPadrao.getNome());



        return novoUsuario;
    }
}