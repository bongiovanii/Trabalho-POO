package edu.fatec.paciente.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.paciente.model.Paciente;

public class PacienteDAO {

    /**
     * Insere um novo paciente no banco de dados
     * @param paciente Paciente a ser inserido
     * @return true se inserido com sucesso, false caso contrário
     */
    public static boolean inserir(Paciente paciente) {
        String sql = "INSERT INTO paciente (nome, cpf, telefone, email, endereco, data_nascimento, peso, altura) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conexao = ConexaoDB.getConnection();
            PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getEmail());
            stmt.setString(5, paciente.getEndereco());
            stmt.setDate(6, Date.valueOf(paciente.getDataNascimento()));
            stmt.setDouble(7, paciente.getPeso());
            stmt.setDouble(8, paciente.getAltura());
            
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao inserir paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Pesquisa um paciente pelo nome
     * @param nome Nome do paciente
     * @return Paciente encontrado ou null
     */
    public static Paciente pesquisarPorNome(String nome) {
        String sql = "SELECT * FROM paciente WHERE nome LIKE ?";
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetParaPaciente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar paciente: " + e.getMessage());
        }
        return null;
    }

    /**
     * Pesquisa um paciente pelo ID
     * @param id ID do paciente
     * @return Paciente encontrado ou null
     */
    public static Paciente pesquisarPorId(Long id) {
        String sql = "SELECT * FROM paciente WHERE id = ?";
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return resultSetParaPaciente(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao pesquisar paciente: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtém todos os pacientes do banco de dados
     * @return Lista de pacientes
     */
    public static List<Paciente> obterTodos() {
        String sql = "SELECT * FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                pacientes.add(resultSetParaPaciente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    /**
     * Atualiza os dados de um paciente
     * @param paciente Paciente com dados atualizados
     * @return true se atualizado com sucesso, false caso contrário
     */
    public static boolean atualizar(Paciente paciente) {
        String sql = "UPDATE paciente SET nome = ?, cpf = ?, telefone = ?, email = ?, " +
                     "endereco = ?, data_nascimento = ?, peso = ?, altura = ? WHERE id = ?";
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getNome());
            stmt.setString(2, paciente.getCpf());
            stmt.setString(3, paciente.getTelefone());
            stmt.setString(4, paciente.getEmail());
            stmt.setString(5, paciente.getEndereco());
            stmt.setDate(6, Date.valueOf(paciente.getDataNascimento()));
            stmt.setDouble(7, paciente.getPeso());
            stmt.setDouble(8, paciente.getAltura());
            stmt.setLong(9, paciente.getId());
            
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtém o próximo ID disponível (maior ID + 1)
     * @return Próximo ID disponível
     */
    public static Long obterProximoId() {
        String sql = "SELECT MAX(id) as max_id FROM paciente";
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                Long maxId = rs.getLong("max_id");
                if (rs.wasNull()) {
                    // Tabela vazia, começar do 1
                    return 1L;
                }
                return maxId + 1;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao obter próximo ID: " + e.getMessage());
        }
        return 1L;
    }

    /**
     * Deleta um paciente do banco de dados
     * @param id ID do paciente a deletar
     * @return true se deletado com sucesso, false caso contrário
     */
    public static boolean deletar(Long id) {
        String sql = "DELETE FROM paciente WHERE id = ?";
        
        try (Connection conexao = ConexaoDB.getConnection();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar paciente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converte um ResultSet em um objeto Paciente
     * @param rs ResultSet contendo os dados
     * @return Objeto Paciente
     * @throws SQLException se houver erro ao acessar o ResultSet
     */
    private static Paciente resultSetParaPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getLong("id"));
        paciente.setNome(rs.getString("nome"));
        paciente.setCpf(rs.getString("cpf"));
        paciente.setTelefone(rs.getString("telefone"));
        paciente.setEmail(rs.getString("email"));
        paciente.setEndereco(rs.getString("endereco"));
        paciente.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
        paciente.setPeso(rs.getDouble("peso"));
        paciente.setAltura(rs.getDouble("altura"));
        return paciente;
    }
}
