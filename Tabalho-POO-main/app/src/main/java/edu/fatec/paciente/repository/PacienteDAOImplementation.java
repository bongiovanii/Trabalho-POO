package edu.fatec.paciente.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.fatec.paciente.model.Paciente;

public class PacienteDAOImplementation implements PacienteDAO {
    // Criando constantes para a conexão com o banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "admin26";

    // Instanciando nova conexão com o banco de dados
    private Connection connection;

    public PacienteDAOImplementation() {
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

    // insere novo paciente no banco de dados
    @Override
    public void inserir(Paciente p) {
        // query SQL para inserir um novo paciente na tabela "paciente"
        String sql = "INSERT INTO paciente (id, nome, cpf, telefone, email, endereco, data_nascimento, peso, altura) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // usando try-with-resources para garantir que o PreparedStatement seja fechado
        // automaticamente
        try {

            // preparando a declaração SQL com os valores do paciente (Statement)
            PreparedStatement stmt = connection.prepareStatement(sql);

            // Troca os "?" na query pelos valores do paciente usando os métodos setXXX do
            // PreparedStatement
            stmt.setLong(1, p.getId());
            stmt.setString(2, p.getNome());
            stmt.setString(3, p.getCpf());
            stmt.setString(4, p.getTelefone());
            stmt.setString(5, p.getEmail());
            stmt.setString(6, p.getEndereco());
            if (p.getDataNascimento() != null) {
                stmt.setDate(7, Date.valueOf(p.getDataNascimento()));
            } else {
                stmt.setNull(7, java.sql.Types.DATE);
            }
            stmt.setDouble(8, p.getPeso());
            stmt.setDouble(9, p.getAltura());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir paciente no banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public void atualizar(long id, Paciente p) {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, telefone = ?, email = ?, endereco = ?, data_nascimento = ?, peso = ?, altura = ? WHERE id = ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getCpf());
            stmt.setString(3, p.getTelefone());
            stmt.setString(4, p.getEmail());
            stmt.setString(5, p.getEndereco());
            if (p.getDataNascimento() != null) {
                stmt.setDate(6, Date.valueOf(p.getDataNascimento()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            stmt.setDouble(7, p.getPeso());
            stmt.setDouble(8, p.getAltura());
            stmt.setLong(9, id);

            stmt.executeUpdate();

            System.out.println("Paciente atualizado com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar paciente no banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public List<Paciente> pesquisarPorNome(String nome) {
        List<Paciente> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM paciente WHERE nome LIKE ?";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getLong("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setTelefone(rs.getString("telefone"));
                p.setEmail(rs.getString("email"));
                p.setEndereco(rs.getString("endereco"));
                p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                p.setPeso(rs.getDouble("peso"));
                p.setAltura(rs.getDouble("altura"));
                lista.add(p);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public void excluir(Paciente p) {
        try {
            String sql = "DELETE FROM paciente WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, p.getId());
            stmt.executeUpdate();
            System.out.println("Paciente excluído com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao excluir paciente no banco de dados.");
            e.printStackTrace();
        }
    }

}
