package edu.fatec.medico.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import edu.fatec.medico.model.Medico;

public class MedicoDAOImplementation implements MedicoDAO {
    // Criando as variáveis para acessar a conexao com o banco de dados

    // URL de conexão com o banco de dados MySQL, incluindo parâmetros paracriar o banco de dados se ele não existir
    private static final String URL = "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private Connection connection;

    public MedicoDAOImplementation() {
        try {
            // Carregando o driver JDBC para MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC carregado com sucesso.");

            // Estabelecendo a conexão com o banco de dados
            connection = java.sql.DriverManager.getConnection(URL, DB_USER, DB_PASS);
            System.out.println("Conexão com o banco de dados estabelecida com sucesso.");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro: Driver Mysql não encontrado.");
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public void inserir(Medico m) {
        // query insert
        String sql = "INSERT INTO medico (id, nome, crm, cpf, especialidade, telefone) VALUES (?, ?, ?, ?, ?, ?)";

        // preparando a declaração SQL com os valores do medico (Statement)
        
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setLong(1, m.getId());
            stmt.setString(2, m.getNome());
            stmt.setLong(3, m.getCrm());
            stmt.setLong(4, m.getCpf());
            stmt.setString(5, m.getEspecialidade());
            stmt.setString(6, m.getTelefone());
            stmt.executeUpdate();
            System.out.println("Médico inserido com sucesso.");
        } catch (Exception e){
             System.out.println("Erro ao inserir médico no banco de dados.");
            e.printStackTrace();
        }
       
       
    }

    @Override
    public void atualizar(long id, Medico m) {
        String sql = "UPDATE medico SET nome = ?, crm = ?, cpf = ?, especialidade = ?, telefone = ? WHERE id = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, m.getNome());
            stmt.setLong(2, m.getCrm());
            stmt.setLong(3, m.getCpf());
            stmt.setString(4, m.getEspecialidade());
            stmt.setString(5, m.getTelefone());
            stmt.setLong(6, id);
            stmt.executeUpdate();
            System.out.println("Médico atualizado com sucesso.");
        }catch (Exception e){
            System.out.println("Erro ao atualizar médico no banco de dados.");
            e.printStackTrace();
        }
       

    }

    @Override
    public void excluir(Medico m) {
        String sql = "DELETE FROM medico WHERE id = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, m.getId());
            stmt.executeUpdate();
            System.out.println("Médico excluído com sucesso.");
        }catch (Exception e){
            System.out.println("Erro ao excluir médico no banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Medico> pesquisarPorNome(String nome) {
        List<Medico> lista =  new java.util.ArrayList<>();
        String sql = "SELECT * FROM medico WHERE nome LIKE ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Medico m =  new Medico();
                m.setId(rs.getLong("id"));
                m.setNome(rs.getString("nome")); 
                m.setCrm(rs.getLong("crm"));
                m.setCpf(rs.getLong("cpf"));
                m.setEspecialidade(rs.getString("especialidade"));
                m.setTelefone(rs.getString("telefone"));
                lista.add(m);
            }
        }catch (Exception e){
            System.out.println("Erro ao pesquisar médico no banco de dados.");
            e.printStackTrace();
        }

        return lista;
    }
    // Estabelecendo as constan

   
    
}
