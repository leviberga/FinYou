package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.factory.ConnectionManager;
import br.com.fiap.fintech.model.Pessoa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO {

    private Connection connection;

    public PessoaDAO() throws SQLException{
        this.connection = ConnectionManager.getConnection();
    }

    public void inserir(Pessoa pessoa) throws SQLException {
        String sql = "INSERT INTO T_PESSOA (NOM_PESSOA, CPF_PESSOA, DAT_NASCIMENTO) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"COD_PESSOA"})) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setDate(3, Date.valueOf(pessoa.getDataNascimento()));

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                pessoa.setCodigo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Pessoa pessoa) throws SQLException {
        String sql = "UPDATE T_PESSOA SET NOM_PESSOA = ?, CPF_PESSOA = ?, DAT_NASCIMENTO = ? WHERE COD_PESSOA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setDate(3, Date.valueOf(pessoa.getDataNascimento()));
            stmt.setInt(4, pessoa.getCodigo());

            stmt.executeUpdate();
        }
    }

    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM T_PESSOA WHERE COD_PESSOA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public Pessoa buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM T_PESSOA WHERE COD_PESSOA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairPessoaDoResultSet(rs);
            }
        }

        return null;
    }

    public Pessoa buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM T_PESSOA WHERE CPF_PESSOA = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairPessoaDoResultSet(rs);
            }
        }

        return null;
    }

    public List<Pessoa> listarTodos() throws SQLException {
        List<Pessoa> pessoas = new ArrayList<>();
        String sql = "SELECT * FROM T_PESSOA ORDER BY NOM_PESSOA";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pessoas.add(extrairPessoaDoResultSet(rs));
            }
        }

        return pessoas;
    }

    private Pessoa extrairPessoaDoResultSet(ResultSet rs) throws SQLException {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigo(rs.getInt("COD_PESSOA"));
        pessoa.setNome(rs.getString("NOM_PESSOA"));
        pessoa.setCpf(rs.getString("CPF_PESSOA"));

        Date dataNascimento = rs.getDate("DAT_NASCIMENTO");
        if (dataNascimento != null) {
            pessoa.setDataNascimento(dataNascimento.toLocalDate());
        }

        return pessoa;
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