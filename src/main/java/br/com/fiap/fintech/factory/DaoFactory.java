package br.com.fiap.fintech.factory;

import br.com.fiap.fintech.dao.*;

import java.sql.SQLException;

public class DaoFactory {

    public static ContaDAO getContaDAO() throws SQLException {
        return new ContaDAO();
    }

    public static InvestimentoDAO getInvestimentoDAO() throws SQLException {
        return new InvestimentoDAO();
    }

    public static PessoaDAO getPessoaDAO() throws SQLException{
        return new PessoaDAO();
    }

    public static TransacaoDAO getTransacaoDAO() throws SQLException{
        return new TransacaoDAO();
    }

    public static UsuarioDAO getUsuarioDAO() throws SQLException{
        return new UsuarioDAO();
    }
}