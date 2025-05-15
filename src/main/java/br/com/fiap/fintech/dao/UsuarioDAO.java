package br.com.fiap.fintech.dao;

import br.com.fiap.fintech.factory.ConnectionFactory;
import br.com.fiap.fintech.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() throws SQLException  {
        this.connection = ConnectionFactory.getConnection();
    }

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO T_USUARIO (EMAIL_USUARIO, PWD_USUARIO, STA_ATIVO, COD_PESSOA) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, new String[] {"COD_USUARIO"})) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.isAtivo() ? "S" : "N");
            stmt.setInt(4, usuario.getCodigoPessoa());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setCodigo(rs.getInt(1));
            }
        }
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE T_USUARIO SET EMAIL_USUARIO = ?, PWD_USUARIO = ?, STA_ATIVO = ?, COD_PESSOA = ? WHERE COD_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.isAtivo() ? "S" : "N");
            stmt.setInt(4, usuario.getCodigoPessoa());
            stmt.setInt(5, usuario.getCodigo());

            stmt.executeUpdate();
        }
    }

    public void excluir(int codigo) throws SQLException {
        String sql = "DELETE FROM T_USUARIO WHERE COD_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            stmt.executeUpdate();
        }
    }

    public Usuario buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT * FROM T_USUARIO WHERE COD_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairUsuarioDoResultSet(rs);
            }
        }

        return null;
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM T_USUARIO WHERE EMAIL_USUARIO = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairUsuarioDoResultSet(rs);
            }
        }

        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM T_USUARIO ORDER BY EMAIL_USUARIO";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }

        return usuarios;
    }

    public List<Usuario> listarPorPessoa(int codigoPessoa) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM T_USUARIO WHERE COD_PESSOA = ? ORDER BY EMAIL_USUARIO";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, codigoPessoa);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }

        return usuarios;
    }

    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM T_USUARIO WHERE EMAIL_USUARIO = ? AND PWD_USUARIO = ? AND STA_ATIVO = 'S'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairUsuarioDoResultSet(rs);
            }
        }

        return null;
    }

    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setCodigo(rs.getInt("COD_USUARIO"));
        usuario.setEmail(rs.getString("EMAIL_USUARIO"));
        usuario.setSenha(rs.getString("PWD_USUARIO"));
        usuario.setAtivo(rs.getString("STA_ATIVO").equals("S"));
        usuario.setCodigoPessoa(rs.getInt("COD_PESSOA"));

        return usuario;
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