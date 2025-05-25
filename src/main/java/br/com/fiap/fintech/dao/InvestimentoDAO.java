package br.com.fiap.fintech.dao;
import br.com.fiap.fintech.factory.ConnectionManager;
import br.com.fiap.fintech.model.Investimento;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class InvestimentoDAO{

    private Connection connection;

    public InvestimentoDAO() throws SQLException {
        this.connection = ConnectionManager.getConnection();
    }

    public void inserir(Investimento investimento) throws SQLException {
        String sql = "INSERT INTO T_INVESTIMENTO (NOM_INVESTIMENTO, TIP_INVESTIMENTO, VLR_APLICADO, DAT_APLICACAO, VLR_ATUAL, COD_USUARIO, COD_CONTA) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"COD_INVESTIMENTO"})) {
            stmt.setString(1, investimento.getNome());
            stmt.setString(2, investimento.getTipo());
            stmt.setBigDecimal(3, investimento.getValorAplicado());
            stmt.setDate(4, Date.valueOf(investimento.getDataAplicacao()));
            stmt.setBigDecimal(5, investimento.getValorAtual());
            stmt.setInt(6, investimento.getCodigoUsuario());

            if (investimento.getCodigoConta() != null) {
                stmt.setInt(7, investimento.getCodigoConta());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                investimento.setCodigo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Investimento investimento) throws SQLException {
        String sql = "UPDATE T_INVESTIMENTO SET NOM_INVESTIMENTO = ?, TIP_INVESTIMENTO = ?, VLR_APLICADO = ?, DAT_APLICACAO = ?, VLR_ATUAL = ?, COD_USUARIO = ?, COD_CONTA = ? WHERE COD_INVESTIMENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, investimento.getNome());
            stmt.setString(2, investimento.getTipo());
            stmt.setBigDecimal(3, investimento.getValorAplicado());
            stmt.setDate(4, Date.valueOf(investimento.getDataAplicacao()));
            stmt.setBigDecimal(5, investimento.getValorAtual());
            stmt.setInt(6, investimento.getCodigoUsuario());

            if (investimento.getCodigoConta() != null) {
                stmt.setInt(7, investimento.getCodigoConta());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

            stmt.setInt(8, investimento.getCodigo());

            stmt.executeUpdate();
        }
    }

    public void atualizarValorAtual(int codigoInvestimento, java.math.BigDecimal valorAtual) throws SQLException {
        String sql = "UPDATE T_INVESTIMENTO SET VLR_ATUAL = ? WHERE COD_INVESTIMENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, valorAtual);
            stmt.setInt(2, codigoInvestimento);

            stmt.executeUpdate();
        }
    }

    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM T_INVESTIMENTO WHERE COD_INVESTIMENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public Investimento buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE COD_INVESTIMENTO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairInvestimentoDoResultSet(rs);
            }
        }

        return null;
    }

    public List<Investimento> listarTodos() throws SQLException {
        List<Investimento> investimentos = new ArrayList<>();
        String sql = "SELECT * FROM T_INVESTIMENTO ORDER BY NOM_INVESTIMENTO";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                investimentos.add(extrairInvestimentoDoResultSet(rs));
            }
        }

        return investimentos;
    }

    public List<Investimento> listarPorUsuario(int codigoUsuario) throws SQLException {
        List<Investimento> investimentos = new ArrayList<>();
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE COD_USUARIO = ? ORDER BY NOM_INVESTIMENTO";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoUsuario);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                investimentos.add(extrairInvestimentoDoResultSet(rs));
            }
        }

        return investimentos;
    }

    public List<Investimento> listarPorConta(int codigoConta) throws SQLException {
        List<Investimento> investimentos = new ArrayList<>();
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE COD_CONTA = ? ORDER BY NOM_INVESTIMENTO";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoConta);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                investimentos.add(extrairInvestimentoDoResultSet(rs));
            }
        }

        return investimentos;
    }

    public List<Investimento> listarPorTipo(String tipo) throws SQLException {
        List<Investimento> investimentos = new ArrayList<>();
        String sql = "SELECT * FROM T_INVESTIMENTO WHERE TIP_INVESTIMENTO = ? ORDER BY NOM_INVESTIMENTO";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                investimentos.add(extrairInvestimentoDoResultSet(rs));
            }
        }

        return investimentos;
    }

    public java.math.BigDecimal calcularTotalInvestido(int codigoUsuario) throws SQLException {
        String sql = "SELECT SUM(VLR_APLICADO) FROM T_INVESTIMENTO WHERE COD_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }

        return java.math.BigDecimal.ZERO;
    }

    public java.math.BigDecimal calcularValorAtualTotal(int codigoUsuario) throws SQLException {
        String sql = "SELECT SUM(VLR_ATUAL) FROM T_INVESTIMENTO WHERE COD_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoUsuario);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }

        return java.math.BigDecimal.ZERO;
    }

    private Investimento extrairInvestimentoDoResultSet(ResultSet rs) throws SQLException {
        Investimento investimento = new Investimento();
        investimento.setCodigo(rs.getInt("COD_INVESTIMENTO"));
        investimento.setNome(rs.getString("NOM_INVESTIMENTO"));
        investimento.setTipo(rs.getString("TIP_INVESTIMENTO"));
        investimento.setValorAplicado(rs.getBigDecimal("VLR_APLICADO"));

        Date dataAplicacao = rs.getDate("DAT_APLICACAO");
        if (dataAplicacao != null) {
            investimento.setDataAplicacao(dataAplicacao.toLocalDate());
        } else {
            investimento.setDataAplicacao(LocalDate.now());
        }

        investimento.setValorAtual(rs.getBigDecimal("VLR_ATUAL"));
        investimento.setCodigoUsuario(rs.getInt("COD_USUARIO"));

        int codigoConta = rs.getInt("COD_CONTA");
        if (!rs.wasNull()) {
            investimento.setCodigoConta(codigoConta);
        }

        return investimento;
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