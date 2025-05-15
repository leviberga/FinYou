package br.com.fiap.fintech.service;

import br.com.fiap.fintech.dao.UsuarioDAO;
import br.com.fiap.fintech.model.Usuario;

import java.sql.SQLException;

public class LoginService {

    private UsuarioDAO usuarioDAO;

    public LoginService() throws SQLException {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Tenta autenticar um usuário com base no email e senha.
     *
     * @param email O email fornecido.
     * @param senha A senha fornecida (deve corresponder à armazenada, CUIDADO com hashing).
     * @return O objeto Usuario se a autenticação for bem-sucedida, null caso contrário.
     * @throws SQLException Se ocorrer um erro no banco de dados.
     */
    public Usuario autenticar(String email, String senha) throws SQLException {

        Usuario usuario = usuarioDAO.autenticar(email, senha);

        if (usuario != null) {
            System.out.println("Login bem-sucedido para: " + usuario.getEmail());
        } else {
            System.out.println("Falha no login para: " + email + ". Verifique email/senha.");
        }

        // usuarioDAO.fecharConexao(); // Opcional
        return usuario;
    }
}