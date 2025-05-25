package br.com.fiap.fintech.view;
import br.com.fiap.fintech.factory.ConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;


public class Conexao
{
    public static void main( String[] args )
    {
        try {
            Connection conexao = ConnectionManager.getConnection();
            System.out.println("Conexão realizada!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}