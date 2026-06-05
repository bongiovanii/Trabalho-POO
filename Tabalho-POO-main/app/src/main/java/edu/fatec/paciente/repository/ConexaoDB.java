package edu.fatec.paciente.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoDB {
    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/clinica";
    private static final String USER = "gustavo";
    private static final String PASSWORD = "admin"; 

    /**
     * Obtém uma conexão com o banco de dados MySQL
     * @return Connection objeto de conexão
     * @throws SQLException se houver erro na conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Carrega o driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Cria a conexão
            Connection conexao = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão com banco de dados estabelecida com sucesso!");
            return conexao;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL não encontrado!");
            throw new SQLException("Driver não encontrado", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Fecha uma conexão com o banco de dados
     * @param conexao Connection objeto a ser fechado
     */
    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão fechada com sucesso!");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Testa a conexão com o banco de dados
     */
    public static void testarConexao() {
        try {
            Connection conexao = getConnection();
            fecharConexao(conexao);
            System.out.println("Teste de conexão realizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Teste de conexão falhou: " + e.getMessage());
        }
    }
}
