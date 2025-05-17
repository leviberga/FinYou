package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.factory.ConnectionFactory;
import br.com.fiap.fintech.model.Conta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ContaDAO {

    private Connection connection;

    public ContaDAO() throws SQLException {
        this.connection = ConnectionFactory.getConnection();
    }

    public void inserir(Conta conta) throws SQLException {
        String sql = "INSERT INTO T_CONTA (NOM_CONTA, TIP_CONTA, SLD_CONTA, COD_USUARIO) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"COD_CONTA"})) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo());
            stmt.setBigDecimal(3, conta.getSaldo());
            stmt.setInt(4, conta.getCodigoUsuario());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                conta.setCodigo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Conta conta) throws SQLException {
        String sql = "UPDATE T_CONTA SET NOM_CONTA = ?, TIP_CONTA = ?, SLD_CONTA = ?, COD_USUARIO = ? WHERE COD_CONTA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, conta.getNome());
            stmt.setString(2, conta.getTipo());
            stmt.setBigDecimal(3, conta.getSaldo());
            stmt.setInt(4, conta.getCodigoUsuario());
            stmt.setInt(5, conta.getCodigo());

            stmt.executeUpdate();
        }
    }

    public void atualizarSaldo(int codigoConta, java.math.BigDecimal novoSaldo) throws SQLException {
        String sql = "UPDATE T_CONTA SET SLD_CONTA = ? WHERE COD_CONTA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novoSaldo);
            stmt.setInt(2, codigoConta);

            stmt.executeUpdate();
        }
    }

    public java.math.BigDecimal calcularSaldoTotalPorUsuario(int codigoUsuario) throws SQLException {
        String sql = "SELECT SUM(SLD_CONTA) FROM T_CONTA WHERE COD_USUARIO = ?";
        java.math.BigDecimal saldoTotal = java.math.BigDecimal.ZERO;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoUsuario);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                java.math.BigDecimal sum = rs.getBigDecimal(1);
                if (sum != null) {
                    saldoTotal = sum;
                }
            }
        }
        return saldoTotal;
    }

    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM T_CONTA WHERE COD_CONTA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public Conta buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM T_CONTA WHERE COD_CONTA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairContaDoResultSet(rs);
            }
        }

        return null;
    }

    public List<Conta> listarTodos() throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM T_CONTA ORDER BY NOM_CONTA";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                contas.add(extrairContaDoResultSet(rs));
            }
        }

        return contas;
    }

    public List<Conta> listarPorUsuario(int codigoUsuario) throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM T_CONTA WHERE COD_USUARIO = ? ORDER BY NOM_CONTA";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoUsuario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(extrairContaDoResultSet(rs));
            }
        }

        return contas;
    }

    public List<Conta> listarPorTipo(String tipo) throws SQLException {
        List<Conta> contas = new ArrayList<>();
        String sql = "SELECT * FROM T_CONTA WHERE TIP_CONTA = ? ORDER BY NOM_CONTA";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                contas.add(extrairContaDoResultSet(rs));
            }
        }

        return contas;
    }

    private Conta extrairContaDoResultSet(ResultSet rs) throws SQLException {
        Conta conta = new Conta();
        conta.setCodigo(rs.getInt("COD_CONTA"));
        conta.setNome(rs.getString("NOM_CONTA"));
        conta.setTipo(rs.getString("TIP_CONTA"));
        conta.setSaldo(rs.getBigDecimal("SLD_CONTA"));
        conta.setCodigoUsuario(rs.getInt("COD_USUARIO"));

        return conta;
    }
}

