package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.model.Transacao;
import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.model.Conta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransacaoService {

    private TransacaoDAO transacaoDAO;
    private ContaDAO contaDAO;

    public TransacaoService() throws SQLException {
        this.transacaoDAO = new TransacaoDAO();
        this.contaDAO = new ContaDAO();
    }

    /**
     * Realiza um depósito (Receita) em uma conta específica.
     *
     * @param usuarioLogado O usuário que está realizando a ação.
     * @param codigoContaDestino O código da conta que receberá o depósito.
     * @param valor O valor a ser depositado.
     * @param descricao Uma descrição opcional para a transação.
     * @return A transação criada.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws IllegalArgumentException Se a conta não for encontrada ou não pertencer ao usuário.
     */
    public Transacao realizarDeposito(Usuario usuarioLogado, int codigoContaDestino, BigDecimal valor, String descricao)
            throws SQLException, IllegalArgumentException {

        validarContaUsuario(usuarioLogado, codigoContaDestino);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo.");
        }

        Transacao deposito = new Transacao();
        deposito.setData(LocalDate.now());
        deposito.setValor(valor);
        deposito.setTipo("RECEITA"); // Tipo definido no BD
        deposito.setDescricao(descricao != null ? descricao : "Depósito em conta");
        deposito.setCodigoContaOrigem(codigoContaDestino); // Para RECEITA/DESPESA, usamos a mesma conta como "origem" lógica no DAO
        deposito.setCodigoContaDestino(null); // Não há destino em depósito

        transacaoDAO.inserir(deposito); // O DAO já atualiza o saldo da conta

        System.out.println("Depósito realizado com sucesso na conta " + codigoContaDestino + ", Valor: " + valor);
        return deposito;
    }

    /**
     * Realiza um saque ou pagamento (Despesa) de uma conta específica.
     *
     * @param usuarioLogado O usuário que está realizando a ação.
     * @param codigoContaOrigem O código da conta de onde o valor será debitado.
     * @param valor O valor a ser debitado.
     * @param descricao Uma descrição opcional para a transação.
     * @return A transação criada.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws IllegalArgumentException Se a conta não for encontrada, não pertencer ao usuário ou não tiver saldo suficiente.
     */
    public Transacao realizarDespesa(Usuario usuarioLogado, int codigoContaOrigem, BigDecimal valor, String descricao)
            throws SQLException, IllegalArgumentException {

        Conta contaOrigem = validarContaUsuario(usuarioLogado, codigoContaOrigem);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da despesa deve ser positivo.");
        }

        // Verificar saldo
        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta " + codigoContaOrigem + ". Saldo: " + contaOrigem.getSaldo());
        }

        Transacao despesa = new Transacao();
        despesa.setData(LocalDate.now());
        despesa.setValor(valor);
        despesa.setTipo("DESPESA"); // Tipo definido no BD
        despesa.setDescricao(descricao != null ? descricao : "Saque/Pagamento");
        despesa.setCodigoContaOrigem(codigoContaOrigem);
        despesa.setCodigoContaDestino(null); // Não há destino em despesa

        transacaoDAO.inserir(despesa); // O DAO já atualiza o saldo da conta

        System.out.println("Despesa realizada com sucesso da conta " + codigoContaOrigem + ", Valor: " + valor);
        return despesa;
    }

    /**
     * Realiza uma transferência entre duas contas.
     *
     * @param usuarioLogado O usuário que está realizando a ação.
     * @param codigoContaOrigem O código da conta de onde o valor será debitado.
     * @param codigoContaDestino O código da conta que receberá o valor.
     * @param valor O valor a ser transferido.
     * @param descricao Uma descrição opcional para a transação.
     * @return A transação criada.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws IllegalArgumentException Se alguma das contas não for encontrada, a conta de origem não pertencer ao usuário ou não tiver saldo suficiente.
     */
    public Transacao realizarTransferencia(Usuario usuarioLogado, int codigoContaOrigem, int codigoContaDestino, BigDecimal valor, String descricao)
            throws SQLException, IllegalArgumentException {

        if (codigoContaOrigem == codigoContaDestino) {
            throw new IllegalArgumentException("Conta de origem e destino não podem ser iguais.");
        }

        Conta contaOrigem = validarContaUsuario(usuarioLogado, codigoContaOrigem);

        // Validar conta de destino (apenas verificar se existe)
        Conta contaDestino = contaDAO.buscarPorCodigo(codigoContaDestino);
        if (contaDestino == null) {
            throw new IllegalArgumentException("Conta de destino (" + codigoContaDestino + ") não encontrada.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo.");
        }

        // Verificar saldo
        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta " + codigoContaOrigem + ". Saldo: " + contaOrigem.getSaldo());
        }

        Transacao transferencia = new Transacao();
        transferencia.setData(LocalDate.now());
        transferencia.setValor(valor);
        transferencia.setTipo("TRANSFERENCIA"); // Tipo definido no BD
        transferencia.setDescricao(descricao != null ? descricao : "Transferência entre contas");
        transferencia.setCodigoContaOrigem(codigoContaOrigem);
        transferencia.setCodigoContaDestino(codigoContaDestino);

        transacaoDAO.inserir(transferencia); // O DAO já atualiza o saldo de ambas as contas

        System.out.println("Transferência realizada com sucesso de " + codigoContaOrigem + " para " + codigoContaDestino + ", Valor: " + valor);
        return transferencia;
    }

    /**
     * Método auxiliar para validar se uma conta existe e pertence ao usuário logado.
     * @param usuarioLogado Usuário autenticado.
     * @param codigoConta Código da conta a ser validada.
     * @return O objeto Conta se for válido.
     * @throws SQLException Erro de banco.
     * @throws IllegalArgumentException Se a conta não existe ou não pertence ao usuário.
     */
    private Conta validarContaUsuario(Usuario usuarioLogado, int codigoConta) throws SQLException, IllegalArgumentException {
        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário não está logado.");
        }
        Conta conta = contaDAO.buscarPorCodigo(codigoConta);
        if (conta == null) {
            throw new IllegalArgumentException("Conta com código " + codigoConta + " não encontrada.");
        }
        // Verifica se o código do usuário da conta bate com o código do usuário logado
        if (!conta.getCodigoUsuario().equals(usuarioLogado.getCodigo())) {
            throw new IllegalArgumentException("A conta " + codigoConta + " não pertence ao usuário logado.");
        }
        return conta;
    }


}