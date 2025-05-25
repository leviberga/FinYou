// br/com/fiap/fintech/manager/ConnectionManager.java
package br.com.fiap.fintech.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl";
    private static final String USER = "RM560489";
    private static final String PASSWORD = "080205";

    static {
        try {
            System.out.println("INFO_CONNECTION_MANAGER: Carregando o driver Oracle JDBC...");
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("SUCESSO_CONNECTION_MANAGER: Driver carregado com sucesso!");
        } catch (ClassNotFoundException e) {
            System.err.println("ERRO_CONNECTION_MANAGER: Driver JDBC Oracle não encontrado.");
            e.printStackTrace();
            throw new RuntimeException("Falha ao carregar o driver Oracle JDBC.", e);
        } catch (Throwable t) {
            System.err.println("ERRO_INESPERADO_CONNECTION_MANAGER: Erro inesperado ao carregar o driver.");
            t.printStackTrace();
            throw new RuntimeException("Erro inesperado ao carregar o driver Oracle.", t);
        }
    }

    // Método para obter uma nova conexão
    public static Connection getConnection() throws SQLException {
        System.out.println("INFO_CONNECTION_MANAGER: Obtendo conexão com o banco...");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método utilitário para fechar conexões com segurança
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("INFO_CONNECTION_MANAGER: Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.err.println("ERRO_CONNECTION_MANAGER: Falha ao fechar conexão.");
                e.printStackTrace();
            }
        }
    }

    public static void close(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
                System.out.println("INFO_CONNECTION_MANAGER: PreparedStatement fechado com sucesso.");
            } catch (SQLException e) {
                System.err.println("ERRO_CONNECTION_MANAGER: Falha ao fechar PreparedStatement.");
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                System.out.println("INFO_CONNECTION_MANAGER: ResultSet fechado com sucesso.");
            } catch (SQLException e) {
                System.err.println("ERRO_CONNECTION_MANAGER: Falha ao fechar ResultSet.");
                e.printStackTrace();
            }
        }
    }

    // Fechamento combinado (ResultSet → Statement → Connection)
    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn) {
        close(rs);
        close(stmt);
        close(conn);
    }
}
