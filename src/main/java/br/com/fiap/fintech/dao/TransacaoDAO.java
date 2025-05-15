package br.com.fiap.fintech.dao;


import br.com.fiap.fintech.factory.ConnectionFactory;
import br.com.fiap.fintech.model.Transacao;
import br.com.fiap.fintech.model.Conta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO  {

    private Connection connection;

    public TransacaoDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void inserir(Transacao transacao) throws SQLException {
        String sql = "INSERT INTO T_TRANSACAO (DAT_TRANSACAO, VLR_TRANSACAO, TIP_TRANSACAO, DSC_TRANSACAO, COD_CONTA_ORIGEM, COD_CONTA_DESTINO) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"COD_TRANSACAO"})) {
            stmt.setDate(1, Date.valueOf(transacao.getData()));
            stmt.setBigDecimal(2, transacao.getValor());
            stmt.setString(3, transacao.getTipo());
            stmt.setString(4, transacao.getDescricao());
            stmt.setInt(5, transacao.getCodigoContaOrigem());

            if (transacao.getCodigoContaDestino() != null) {
                stmt.setInt(6, transacao.getCodigoContaDestino());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transacao.setCodigo(rs.getInt(1));
            }

            // Atualiza o saldo das contas
            atualizarSaldosContas(transacao);
        }
    }

    public void atualizar(Transacao transacao) throws SQLException {
        // Primeiro, obtém a transação original para reverter os saldos
        Transacao transacaoOriginal = buscarPorCodigo(transacao.getCodigo());
        if (transacaoOriginal != null) {
            reverterSaldosContas(transacaoOriginal);
        }

        String sql = "UPDATE T_TRANSACAO SET DAT_TRANSACAO = ?, VLR_TRANSACAO = ?, TIP_TRANSACAO = ?, DSC_TRANSACAO = ?, COD_CONTA_ORIGEM = ?, COD_CONTA_DESTINO = ? WHERE COD_TRANSACAO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(transacao.getData()));
            stmt.setBigDecimal(2, transacao.getValor());
            stmt.setString(3, transacao.getTipo());
            stmt.setString(4, transacao.getDescricao());
            stmt.setInt(5, transacao.getCodigoContaOrigem());

            if (transacao.getCodigoContaDestino() != null) {
                stmt.setInt(6, transacao.getCodigoContaDestino());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }

            stmt.setInt(7, transacao.getCodigo());

            stmt.executeUpdate();

            // Atualiza o saldo das contas com a nova transação
            atualizarSaldosContas(transacao);
        }
    }

    public void excluir(int codigo) throws SQLException {
        // Primeiro, obtém a transação para reverter os saldos
        Transacao transacao = buscarPorCodigo(codigo);
        if (transacao != null) {
            reverterSaldosContas(transacao);
        }

        String sql = "DELETE FROM T_TRANSACAO WHERE COD_TRANSACAO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public Transacao buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM T_TRANSACAO WHERE COD_TRANSACAO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairTransacaoDoResultSet(rs);
            }
        }

        return null;
    }

    public List<Transacao> listarTodos() throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM T_TRANSACAO ORDER BY DAT_TRANSACAO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                transacoes.add(extrairTransacaoDoResultSet(rs));
            }
        }

        return transacoes;
    }

    public List<Transacao> listarPorConta(int codigoConta) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM T_TRANSACAO WHERE COD_CONTA_ORIGEM = ? OR COD_CONTA_DESTINO = ? ORDER BY DAT_TRANSACAO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoConta);
            stmt.setInt(2, codigoConta);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(extrairTransacaoDoResultSet(rs));
            }
        }

        return transacoes;
    }

    public List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM T_TRANSACAO WHERE DAT_TRANSACAO BETWEEN ? AND ? ORDER BY DAT_TRANSACAO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(inicio));
            stmt.setDate(2, Date.valueOf(fim));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(extrairTransacaoDoResultSet(rs));
            }
        }

        return transacoes;
    }

    public List<Transacao> listarPorTipo(String tipo) throws SQLException {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT * FROM T_TRANSACAO WHERE TIP_TRANSACAO = ? ORDER BY DAT_TRANSACAO DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transacoes.add(extrairTransacaoDoResultSet(rs));
            }
        }

        return transacoes;
    }

    private Transacao extrairTransacaoDoResultSet(ResultSet rs) throws SQLException {
        Transacao transacao = new Transacao();
        transacao.setCodigo(rs.getInt("COD_TRANSACAO"));

        Date dataTransacao = rs.getDate("DAT_TRANSACAO");
        if (dataTransacao != null) {
            transacao.setData(dataTransacao.toLocalDate());
        } else {
            transacao.setData(LocalDate.now());
        }

        transacao.setValor(rs.getBigDecimal("VLR_TRANSACAO"));
        transacao.setTipo(rs.getString("TIP_TRANSACAO"));
        transacao.setDescricao(rs.getString("DSC_TRANSACAO"));
        transacao.setCodigoContaOrigem(rs.getInt("COD_CONTA_ORIGEM"));

        int codigoContaDestino = rs.getInt("COD_CONTA_DESTINO");
        if (!rs.wasNull()) {
            transacao.setCodigoContaDestino(codigoContaDestino);
        }

        return transacao;
    }

    private void atualizarSaldosContas(Transacao transacao) throws SQLException {
        ContaDAO contaDAO = new ContaDAO();

        // Obtém as contas envolvidas
        Conta contaOrigem = contaDAO.buscarPorCodigo(transacao.getCodigoContaOrigem());

        if (contaOrigem == null) {
            throw new SQLException("Conta de origem não encontrada");
        }

        java.math.BigDecimal valorTransacao = transacao.getValor();

        // Atualiza o saldo da conta de origem com base no tipo de transação
        if ("RECEITA".equals(transacao.getTipo())) {
            // Receita: adiciona ao saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo().add(valorTransacao));
            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
        } else if ("DESPESA".equals(transacao.getTipo())) {
            // Despesa: subtrai do saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransacao));
            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
        } else if ("TRANSFERENCIA".equals(transacao.getTipo()) && transacao.getCodigoContaDestino() != null) {
            // Transferência: subtrai da origem e adiciona ao destino
              Conta contaDestino = contaDAO.buscarPorCodigo(transacao.getCodigoContaDestino());

            if (contaDestino == null) {
                throw new SQLException("Conta de destino não encontrada");
            }

            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransacao));
            contaDestino.setSaldo(contaDestino.getSaldo().add(valorTransacao));

            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
            contaDAO.atualizarSaldo(contaDestino.getCodigo(), contaDestino.getSaldo());
        }
    }

    private void reverterSaldosContas(Transacao transacao) throws SQLException {
        ContaDAO contaDAO = new ContaDAO();

        // Obtém as contas envolvidas
        Conta contaOrigem = contaDAO.buscarPorCodigo(transacao.getCodigoContaOrigem());

        if (contaOrigem == null) {
            throw new SQLException("Conta de origem não encontrada");
        }

        java.math.BigDecimal valorTransacao = transacao.getValor();

        // Reverte o saldo da conta de origem com base no tipo de transação
        if ("RECEITA".equals(transacao.getTipo())) {
            // Receita: subtrai do saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valorTransacao));
            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
        } else if ("DESPESA".equals(transacao.getTipo())) {
            // Despesa: adiciona ao saldo da conta de origem
            contaOrigem.setSaldo(contaOrigem.getSaldo().add(valorTransacao));
            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
        } else if ("TRANSFERENCIA".equals(transacao.getTipo()) && transacao.getCodigoContaDestino() != null) {
            // Transferência: adiciona à origem e subtrai do destino
           Conta contaDestino = contaDAO.buscarPorCodigo(transacao.getCodigoContaDestino());

            if (contaDestino == null) {
                throw new SQLException("Conta de destino não encontrada");
            }

            contaOrigem.setSaldo(contaOrigem.getSaldo().add(valorTransacao));
            contaDestino.setSaldo(contaDestino.getSaldo().subtract(valorTransacao));

            contaDAO.atualizarSaldo(contaOrigem.getCodigo(), contaOrigem.getSaldo());
            contaDAO.atualizarSaldo(contaDestino.getCodigo(), contaDestino.getSaldo());
        }
    }

    public void fecharConexao() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}