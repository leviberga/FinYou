package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dao.ContaDAO;
import br.com.fiap.fintech.dao.PessoaDAO;
import br.com.fiap.fintech.dao.TransacaoDAO;
import br.com.fiap.fintech.factory.DaoFactory;
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

public class DashboardService {

    private ContaDAO contaDAO;
    private TransacaoDAO transacaoDAO;
    private PessoaDAO pessoaDAO;

    public DashboardService() {
        try {
            // Usando DaoFactory para obter as instâncias dos DAOs
            this.contaDAO = DaoFactory.getContaDAO();
            this.transacaoDAO = DaoFactory.getTransacaoDAO();
            this.pessoaDAO = DaoFactory.getPessoaDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar camada de acesso a dados para o dashboard.", e);
        } catch (RuntimeException re) {
            re.printStackTrace();
            throw re;
        }
    }

    public Map<String, Object> carregarDadosDashboard(Usuario usuarioLogado) throws SQLException {
        if (usuarioLogado == null || usuarioLogado.getCodigo() == null) {
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
                    dadosDashboard.put("nomeUsuario", nomeUsuarioDefault);
                }
            } else {
                dadosDashboard.put("nomeUsuario", nomeUsuarioDefault);
            }

            // 2. Calcular totais usando as transações diretamente
            List<Transacao> todasTransacoes = transacaoDAO.listarPorUsuario(usuarioLogado.getCodigo());

            BigDecimal totalReceitas = BigDecimal.ZERO;
            BigDecimal totalDespesas = BigDecimal.ZERO;
            BigDecimal saldoTotal = BigDecimal.ZERO;

            if (todasTransacoes != null && !todasTransacoes.isEmpty()) {
                for (Transacao transacao : todasTransacoes) {
                    BigDecimal valor = transacao.getValor() != null ? transacao.getValor() : BigDecimal.ZERO;

                    if ("RECEITA".equalsIgnoreCase(transacao.getTipo())) {
                        totalReceitas = totalReceitas.add(valor);
                        saldoTotal = saldoTotal.add(valor);
                    } else if ("DESPESA".equalsIgnoreCase(transacao.getTipo()) ||
                            "TRANSFERENCIA".equalsIgnoreCase(transacao.getTipo())) {
                        totalDespesas = totalDespesas.add(valor);
                        saldoTotal = saldoTotal.subtract(valor);
                    }
                }
            }

            // Definir os valores calculados
            dadosDashboard.put("saldoTotal", saldoTotal);
            dadosDashboard.put("totalGanhos", totalReceitas);
            dadosDashboard.put("totalGastos", totalDespesas);

            // 3. Todas as transações (em vez de apenas as recentes)
            // Ordenar por data mais recente primeiro
            if (todasTransacoes != null && !todasTransacoes.isEmpty()) {
                todasTransacoes.sort((t1, t2) -> {
                    if (t1.getData() == null && t2.getData() == null) return 0;
                    if (t1.getData() == null) return 1;
                    if (t2.getData() == null) return -1;
                    return t2.getData().compareTo(t1.getData());
                });
                dadosDashboard.put("transacoesRecentes", todasTransacoes);
            } else {
                dadosDashboard.put("transacoesRecentes", Collections.emptyList());
            }

            // 4. Objeto usuário
            dadosDashboard.put("usuarioLogado", usuarioLogado);

            // Log para debug
            System.out.println("DEBUG - Dashboard calculado:");
            System.out.println("Total de transações: " + (todasTransacoes != null ? todasTransacoes.size() : 0));
            System.out.println("Total Receitas: " + totalReceitas);
            System.out.println("Total Despesas: " + totalDespesas);
            System.out.println("Saldo Total: " + saldoTotal);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro de banco de dados ao carregar informações do dashboard.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao processar informações do dashboard.", e);
        }

        return dadosDashboard;
    }
}