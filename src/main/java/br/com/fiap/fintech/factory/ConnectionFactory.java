// br/com/fiap/fintech/factory/ConnectionFactory.java
package br.com.fiap.fintech.factory; // Ou o pacote correto

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    private static final String USER = "RM560489"; // Substitua pelo seu usuário RM
    private static final String PASSWORD = "080205"; // Substitua pela sua senha

    static {
        try {
            System.out.println("INFO_CONNECTION_FACTORY: Tentando carregar a classe do driver Oracle: oracle.jdbc.OracleDriver");
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("SUCESSO_CONNECTION_FACTORY: Classe do driver Oracle JDBC (oracle.jdbc.OracleDriver) carregada via Class.forName!");
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO_CRITICO_CONNECTION_FACTORY: Classe do driver Oracle JDBC (oracle.jdbc.OracleDriver) NÃO encontrada no classpath.");
            e.printStackTrace(); // MUITO IMPORTANTE VER ISSO NO LOG
            throw new RuntimeException("Falha crítica ao carregar o driver Oracle JDBC. Verifique o ojdbcX.jar em WEB-INF/lib.", e);
        } catch (Throwable t) {
            System.err.println("ERRO_INESPERADO_CONNECTION_FACTORY: Erro inesperado durante o carregamento do driver.");
            t.printStackTrace();
            throw new RuntimeException("Erro inesperado durante o carregamento do driver.", t);
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("INFO_CONNECTION_FACTORY: ConnectionFactory tentando obter conexão para: " + URL);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // (main de teste opcional)
}