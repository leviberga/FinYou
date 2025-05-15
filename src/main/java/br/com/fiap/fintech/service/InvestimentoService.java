package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.dao.InvestimentoDAO;
import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.model.Investimento;
import br.com.fiap.fintech.model.Transacao;
import br.com.fiap.fintech.model.Usuario;
import br.com.fiap.fintech.model.Conta;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class InvestimentoService {

    private InvestimentoDAO investimentoDAO;
    private ContaDAO contaDAO;
    private TransacaoDAO transacaoDAO; // Usado se a aplicação debita de uma conta

    public InvestimentoService() throws SQLException {
        this.investimentoDAO = new InvestimentoDAO();
        this.contaDAO = new ContaDAO();
        this.transacaoDAO = new TransacaoDAO();
    }

    /**
     * Cria um novo investimento para o usuário, opcionalmente debitando o valor de uma conta.
     *
     * @param usuarioLogado O usuário que está realizando o investimento.
     * @param nomeInvestimento Nome descritivo do investimento (ex: Ações XYZ, Tesouro Selic 2029).
     * @param tipoInvestimento Tipo do investimento (ACAO, FUNDO, etc., conforme CHECK do BD).
     * @param valorAplicado O valor inicial aplicado no investimento.
     * @param codigoContaDebito O código da conta de onde debitar o valor (opcional, pode ser null se o dinheiro vem de "fora").
     * @return O objeto Investimento criado.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     * @throws IllegalArgumentException Se dados inválidos forem fornecidos (valor negativo, conta inválida, saldo insuficiente).
     */
    public Investimento aplicarEmInvestimento(Usuario usuarioLogado, String nomeInvestimento, String tipoInvestimento,
                                              BigDecimal valorAplicado, Integer codigoContaDebito)
            throws SQLException, IllegalArgumentException {

        if (usuarioLogado == null) {
            throw new IllegalArgumentException("Usuário não está logado.");
        }
        if (valorAplicado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor aplicado deve ser positivo.");
        }

        Conta contaDebito = null;
        // 1. Validar e preparar débito da conta (se aplicável)
        if (codigoContaDebito != null) {
            contaDebito = contaDAO.buscarPorCodigo(codigoContaDebito);
            if (contaDebito == null) {
                throw new IllegalArgumentException("Conta para débito (" + codigoContaDebito + ") não encontrada.");
            }
            if (!contaDebito.getCodigoUsuario().equals(usuarioLogado.getCodigo())) {
                throw new IllegalArgumentException("A conta de débito " + codigoContaDebito + " não pertence ao usuário logado.");
            }
            if (contaDebito.getSaldo().compareTo(valorAplicado) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente na conta " + codigoContaDebito + ". Saldo: " + contaDebito.getSaldo());
            }
        }

        // 2. Criar o objeto Investimento
        Investimento novoInvestimento = new Investimento();
        novoInvestimento.setNome(nomeInvestimento);
        novoInvestimento.setTipo(tipoInvestimento); // Validar se o tipo é permitido seria bom
        novoInvestimento.setValorAplicado(valorAplicado);
        novoInvestimento.setDataAplicacao(LocalDate.now());
        novoInvestimento.setValorAtual(valorAplicado); // Valor atual inicial é o aplicado
        novoInvestimento.setCodigoUsuario(usuarioLogado.getCodigo());
        novoInvestimento.setCodigoConta(codigoContaDebito); // Vincula ao investimento (pode ser null)

        // --- INÍCIO TRANSAÇÃO (IDEALMENTE) ---
        // Em um cenário real, você usaria connection.setAutoCommit(false),
        // realizaria as operações e depois connection.commit() ou connection.rollback().
        // Como os DAOs abrem e (talvez) fecham conexões, a transação fica mais complexa.
        // Para este exemplo, faremos sequencialmente, cientes do risco.

        try {
            // 3. Inserir o Investimento no BD
            investimentoDAO.inserir(novoInvestimento);
            System.out.println("Investimento '" + nomeInvestimento + "' registrado com sucesso. Código: " + novoInvestimento.getCodigo());

            // 4. Registrar a despesa na conta (se aplicável)
            if (contaDebito != null) {
                Transacao despesaInvestimento = new Transacao();
                despesaInvestimento.setData(LocalDate.now());
                despesaInvestimento.setValor(valorAplicado);
                despesaInvestimento.setTipo("DESPESA");
                despesaInvestimento.setDescricao("Aplicação em " + nomeInvestimento);
                despesaInvestimento.setCodigoContaOrigem(codigoContaDebito);
                despesaInvestimento.setCodigoContaDestino(null);

                transacaoDAO.inserir(despesaInvestimento); // Debita da conta
                System.out.println("Valor de R$" + valorAplicado + " debitado da conta " + codigoContaDebito + " para o investimento.");
            }

            // --- COMMIT (IDEALMENTE) ---

            return novoInvestimento;

        } catch (SQLException e) {
            // --- ROLLBACK (IDEALMENTE) ---
            System.err.println("Erro ao registrar investimento ou debitar conta. Operação pode estar incompleta.");
            // Aqui, seria necessário reverter a inserção do investimento se o débito falhar,
            // ou reverter o débito se a inserção do investimento falhar (depende da ordem).
            throw e; // Propaga a exceção
        }
        // --- FIM TRANSAÇÃO ---

        // Opcional: Fechar conexões
        // fecharConexoes();
    }


    // Método para atualizar valor atual (exemplo, viria de uma cotação externa)
    public void atualizarValorInvestimento(int codigoInvestimento, BigDecimal novoValorAtual) throws SQLException {
        Investimento investimento = investimentoDAO.buscarPorCodigo(codigoInvestimento);
        if(investimento == null) {
            throw new IllegalArgumentException("Investimento não encontrado: " + codigoInvestimento);
        }
        // Poderia ter validação se o usuário logado é dono do investimento
        investimentoDAO.atualizarValorAtual(codigoInvestimento, novoValorAtual);
        System.out.println("Valor atual do investimento " + codigoInvestimento + " atualizado para " + novoValorAtual);
    }


    // Opcional: Fechar conexões
    // public void fecharConexoes() {
    //     try { if (investimentoDAO != null) investimentoDAO.fecharConexao(); } catch(Exception e) { e.printStackTrace(); }
    //     try { if (contaDAO != null) contaDAO.fecharConexao(); } catch(Exception e) { e.printStackTrace(); }
    //     try { if (transacaoDAO != null) transacaoDAO.fecharConexao(); } catch(Exception e) { e.printStackTrace(); }
    // }
}