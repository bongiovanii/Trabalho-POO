package edu.fatec.exame.repository;

import edu.fatec.exame.model.Exame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação concreta do ExameDAO usando JDBC com MariaDB.
 *
 * Responsável por toda a comunicação com o banco de dados:
 * abre a conexão no construtor e executa as operações SQL nos métodos.
 */
public class ExameDAOImplementation implements ExameDAO {

    // String de conexão com o banco MariaDB.
    // O banco 'clinica' é compartilhado com o CRUD de paciente do colega.
    private static final String DB_URI =
        "jdbc:mariadb://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";

    // Credenciais do banco de dados local.
    private static final String DB_USER = "root";
    private static final String DB_PASS = "123456";

    // Conexão reutilizada em todos os métodos desta instância.
    private Connection con;

    /**
     * Construtor: carrega o driver MariaDB e abre a conexão com o banco.
     * É chamado uma vez quando o ExameControl é instanciado.
     */
    public ExameDAOImplementation() {
        try {
            // Registra o driver JDBC do MariaDB no sistema
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Driver carregado...");

            // Abre a conexão com o banco usando as constantes definidas acima
            con = DriverManager.getConnection(DB_URI, DB_USER, DB_PASS);
            System.out.println("Conectado ao banco de dados clinica...");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro: driver MariaDB não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados.");
            e.printStackTrace();
        }
    }

    /**
     * Insere um novo exame na tabela 'exame'.
     * Usa PreparedStatement para evitar SQL Injection e tratar tipos corretamente.
     */
    @Override
    public void cadastrar(Exame e) {
        try {
            // SQL parametrizado: as '?' serão substituídas pelos valores do objeto
            String sql = "INSERT INTO exame (tipo, data_realizacao, resultado, observacao, nome_paciente) " +
                         "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, e.getTipo());
            // Converte LocalDate para java.sql.Date, que o JDBC entende
            stm.setDate(2, java.sql.Date.valueOf(e.getDataRealizacao()));
            stm.setString(3, e.getResultado());
            stm.setString(4, e.getObservacao());
            stm.setString(5, e.getNomePaciente());

            stm.executeUpdate();
            System.out.println("Exame cadastrado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar exame.");
            ex.printStackTrace();
        }
    }

    /**
     * Remove o exame do banco usando o ID como chave.
     */
    @Override
    public void apagar(Exame e) {
        try {
            String sql = "DELETE FROM exame WHERE id = ?";

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setLong(1, e.getId());

            stm.executeUpdate();
            System.out.println("Exame apagado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao apagar exame.");
            ex.printStackTrace();
        }
    }

    /**
     * Atualiza todos os campos de um exame existente, identificado pelo ID.
     */
    @Override
    public void atualizar(long id, Exame e) {
        try {
            String sql = "UPDATE exame SET tipo=?, data_realizacao=?, resultado=?, " +
                         "observacao=?, nome_paciente=? WHERE id=?";

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, e.getTipo());
            stm.setDate(2, java.sql.Date.valueOf(e.getDataRealizacao()));
            stm.setString(3, e.getResultado());
            stm.setString(4, e.getObservacao());
            stm.setString(5, e.getNomePaciente());
            // O ID vai no final, correspondendo ao último '?' da cláusula WHERE
            stm.setLong(6, id);

            stm.executeUpdate();
            System.out.println("Exame atualizado com sucesso.");
        } catch (SQLException ex) {
            System.out.println("Erro ao atualizar exame.");
            ex.printStackTrace();
        }
    }

    /**
     * Busca exames pelo tipo usando LIKE para pesquisa parcial.
     * Monta objetos Exame a partir dos dados retornados pelo banco.
     */
    @Override
    public List<Exame> pesquisarPorTipo(String tipo) {
        List<Exame> lista = new ArrayList<>();
        try {
            // LIKE com '%' nas duas pontas = pesquisa em qualquer posição da string
            String sql = "SELECT * FROM exame WHERE tipo LIKE ?";

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, "%" + tipo + "%");

            ResultSet rs = stm.executeQuery();

            // Para cada linha retornada, cria um objeto Exame e adiciona na lista
            while (rs.next()) {
                Exame e = new Exame();
                e.setId(rs.getLong("id"));
                e.setTipo(rs.getString("tipo"));
                // Converte java.sql.Date de volta para LocalDate do Java
                e.setDataRealizacao(rs.getDate("data_realizacao").toLocalDate());
                e.setResultado(rs.getString("resultado"));
                e.setObservacao(rs.getString("observacao"));
                e.setNomePaciente(rs.getString("nome_paciente"));
                lista.add(e);
            }
            System.out.println("Exames carregados: " + lista.size());
        } catch (SQLException ex) {
            System.out.println("Erro ao pesquisar exames.");
            ex.printStackTrace();
        }
        return lista;
    }
}
