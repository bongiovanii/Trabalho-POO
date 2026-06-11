package edu.fatec.exame.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.exame.model.Exame;

// Implementacao do DAO usando JDBC com MariaDB
public class ExameDAOImplementation implements ExameDAO {

    private static final String DB_URI =
        "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";

    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private Connection conexao;

    // abre a conexao com o banco assim que o objeto e criado
    public ExameDAOImplementation() {
        try {            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver carregado...");
            conexao = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
            System.out.println("Conectado ao banco de dados clinica...");
        } catch(ClassNotFoundException e) {
            System.out.println("Erro: driver MySQL não encontrado.");
            e.printStackTrace();
        } catch(SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }

    // insere um novo registro na tabela exame
    @Override
    public void cadastrar(Exame exame) {
        try {
            String sql = "INSERT INTO exame (tipo, data_realizacao, resultado, observacao, nome_paciente, status) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, exame.getTipo());
            comando.setDate(2, java.sql.Date.valueOf(exame.getDataRealizacao()));
            comando.setString(3, exame.getResultado());
            comando.setString(4, exame.getObservacao());
            comando.setString(5, exame.getNomePaciente());
            comando.setString(6, exame.getStatus());

            comando.executeUpdate();
            System.out.println("Exame cadastrado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar exame.");
            ex.printStackTrace();
        }
    }

    // deleta o exame usando o id como referencia
    @Override
    public void apagar(Exame e) {
        try {
            String sql = "DELETE FROM exame WHERE id = ?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setLong(1, e.getId());

            comando.executeUpdate();
            System.out.println("Exame apagado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao apagar exame.");
            ex.printStackTrace();
        }
    }

    // atualiza todos os campos do exame
    @Override
    public void atualizar(long id, Exame e) {
        try {
            String sql = "UPDATE exame SET tipo=?, data_realizacao=?, resultado=?, " +
                "observacao=?, nome_paciente=?, status=? WHERE id=?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, e.getTipo());
            comando.setDate(2, java.sql.Date.valueOf(e.getDataRealizacao()));
            comando.setString(3, e.getResultado());
            comando.setString(4, e.getObservacao());
            comando.setString(5, e.getNomePaciente());
            comando.setString(6, e.getStatus());
            comando.setLong(7, id);

            comando.executeUpdate();
            System.out.println("Exame atualizado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar exame.");
            ex.printStackTrace();
        }
    }

    // busca por tipo com LIKE - o % nas duas pontas faz pesquisa parcial
    @Override
    public List<Exame> pesquisarPorTipo(String tipo) {
        List<Exame> listaExames = new ArrayList<>();
        try {
            String sql = "SELECT * FROM exame WHERE tipo LIKE ?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, "%" + tipo + "%");

            ResultSet rs = comando.executeQuery();

            while (rs.next()) {
                Exame exame = new Exame();
                exame.setId(rs.getLong("id"));
                exame.setTipo(rs.getString("tipo"));
                // Converte java.sql.Date de volta para LocalDate do Java
                exame.setDataRealizacao(rs.getDate("data_realizacao").toLocalDate());
                exame.setResultado(rs.getString("resultado"));
                exame.setObservacao(rs.getString("observacao"));
                exame.setNomePaciente(rs.getString("nome_paciente"));
                exame.setStatus(rs.getString("status"));
                listaExames.add(exame);
            }
            System.out.println("Exames carregados: " + listaExames.size());
        } catch (SQLException ex) {
            System.out.println("Erro ao pesquisar exames.");
            ex.printStackTrace();
        }
        return listaExames;
    }
}
