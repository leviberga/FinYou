package br.com.fiap.fintech.service; // Certifique-se que este é seu pacote correto

import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.dao.PessoaDAO;
import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.model.Pessoa;
import br.com.fiap.fintech.model.Transacao;
import br.com.fiap.fintech.model.Usuario;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Importe um logger se for usar (ex: SLF4J)
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

public class DashboardService {

    // private static final Logger logger = LoggerFactory.getLogger(DashboardService.class); // Exemplo com SLF4J

    private ContaDAO contaDAO;
    private TransacaoDAO transacaoDAO;
    private PessoaDAO pessoaDAO;

    public DashboardService() {
        try {
            this.contaDAO = new ContaDAO();
            this.transacaoDAO = new TransacaoDAO();
            this.pessoaDAO = new PessoaDAO();
            // logger.info("DAOs do DashboardService inicializados com sucesso."); // Exemplo de log real
        } catch (SQLException e) {
            // logger.error("Falha CRÍTICA ao inicializar DAOs no DashboardService devido a SQLException.", e);
            e.printStackTrace(); // Mantenha para desenvolvimento ou substitua por logger.error
            throw new RuntimeException("Erro ao inicializar camada de acesso a dados para o dashboard.", e);
        } catch (RuntimeException re) {
            // logger.error("Falha CRÍTICA ao inicializar DAOs no DashboardService (RuntimeException).", re);
            re.printStackTrace(); // Mantenha para desenvolvimento ou substitua por logger.error
            throw re;
        }
    }

    public Map<String, Object> carregarDadosDashboard(Usuario usuarioLogado) {
        if (usuarioLogado == null || usuarioLogado.getCodigo() == null) {
            // logger.warn("Tentativa de carregar dados do dashboard com usuário logado nulo ou sem código.");
            throw new IllegalArgumentException("Usuário logado inválido ou não possui código de identificação.");
        }

        Map<String, Object> dadosDashboard = new HashMap<>();
        String nomeUsuarioDefault = "Usuário";

        try {
            // 1. Nome do Usuário
            if (usuarioLogado.getCodigoPessoa() != null) {
                Pessoa pessoa = pessoaDAO.buscarPorCodigo(usuarioLogado.getCodigoPessoa());
                if (pessoa != null && pessoa.getNome() != null && !pessoa.getNome().trim().isEmpty()) {
                    dadosDashboard.put("nomeUsuario", pessoa.getNome());
                } else {
                    // logger.warn("Pessoa não encontrada ou nome vazio para codigoPessoa {}. Usando nome default.", usuarioLogado.getCodigoPessoa());
                    dadosDashboard.put("nomeUsuario", nomeUsuarioDefault);
                }
            } else {
                // logger.warn("codigoPessoa é null para o usuário {}. Usando nome default.", usuarioLogado.getEmail());
                dadosDashboard.put("nomeUsuario", nomeUsuarioDefault);
            }

            // 2. Saldo Total
            BigDecimal saldoTotal = contaDAO.calcularSaldoTotalPorUsuario(usuarioLogado.getCodigo());
            dadosDashboard.put("saldoTotal", saldoTotal != null ? saldoTotal : BigDecimal.ZERO);

            // 3. Total de Entradas e Saídas (para o mês atual)
            LocalDate inicioPeriodo = LocalDate.now().withDayOfMonth(1);
            LocalDate fimPeriodo = LocalDate.now(); // Inclui o dia de hoje

            BigDecimal totalReceitas = transacaoDAO.calcularTotalReceitasPorUsuario(usuarioLogado.getCodigo(), inicioPeriodo, fimPeriodo);
            BigDecimal totalDespesas = transacaoDAO.calcularTotalDespesasPorUsuario(usuarioLogado.getCodigo(), inicioPeriodo, fimPeriodo);

            dadosDashboard.put("totalGanhos", totalReceitas != null ? totalReceitas : BigDecimal.ZERO);
            dadosDashboard.put("totalGastos", totalDespesas != null ? totalDespesas : BigDecimal.ZERO);

            // 4. Transações Recentes (ex: últimas 5)
            List<Transacao> todasTransacoesUsuario = transacaoDAO.listarPorUsuario(usuarioLogado.getCodigo());
            int limiteTransacoesRecentes = 5;
            List<Transacao> transacoesRecentes;
            if (todasTransacoesUsuario != null && !todasTransacoesUsuario.isEmpty()) {
                transacoesRecentes = todasTransacoesUsuario.subList(0, Math.min(todasTransacoesUsuario.size(), limiteTransacoesRecentes));
            } else {
                transacoesRecentes = Collections.emptyList();
            }
            dadosDashboard.put("transacoesRecentes", transacoesRecentes);

            // 5. Colocar o próprio objeto usuário, caso precise de outros dados dele no JSP
            dadosDashboard.put("usuarioLogado", usuarioLogado);

        } catch (SQLException e) {
            // logger.error("SQLException ao carregar dados do dashboard para o usuário ID: {}", usuarioLogado.getCodigo(), e);
            e.printStackTrace(); // Mantenha para desenvolvimento ou substitua por logger.error
            // Em vez de popular com defaults, lançar uma exceção para o Servlet tratar
            // e exibir uma mensagem de erro mais clara para o usuário.
            throw new RuntimeException("Erro de banco de dados ao carregar informações do dashboard.", e);
        } catch (Exception e) {
            // logger.error("Erro GERAL e inesperado ao carregar dados do dashboard para o usuário ID: {}", usuarioLogado.getCodigo(), e);
            e.printStackTrace(); // Mantenha para desenvolvimento ou substitua por logger.error
            throw new RuntimeException("Erro inesperado ao processar informações do dashboard.", e);
        }

        return dadosDashboard;
    }
}