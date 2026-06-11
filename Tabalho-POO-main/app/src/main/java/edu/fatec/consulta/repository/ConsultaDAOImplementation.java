package edu.fatec.consulta.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.fatec.consulta.model.Consulta;

// implementacao concreta do ConsultaDAO usando JDBC com MySQL
// toda comunicacao com o banco de dados acontece aqui
// os outros arquivos (Control, Boundary) nao sabem nada de SQL
public class ConsultaDAOImplementation implements ConsultaDAO {

    private static final String URL_BANCO =
        "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true";

    private static final String USUARIO = "root";
    private static final String SENHA = "";

    private Connection conexao;

    // carrega o driver MySQL e abre a conexao ao criar o objeto
    // a conexao fica aberta e e reutilizada em todos os metodos
    // se der erro aqui, os outros metodos vao falhar com NullPointerException na conexao
    public ConsultaDAOImplementation() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver carregado");
            conexao = DriverManager.getConnection(URL_BANCO, USUARIO, SENHA);
            System.out.println("Conectado ao banco clinica");
        } catch (ClassNotFoundException erro) {
            System.out.println("Driver nao encontrado");
            erro.printStackTrace();
        } catch (SQLException erro) {
            System.out.println("Erro na conexao com o banco");
            erro.printStackTrace();
        }
    }

    // atualiza todos os campos da consulta identificada pelo id
    // o id vai no ultimo ? por causa da clausula WHERE no final do SQL
    // LocalDate precisa ser convertido pra java.sql.Date pro JDBC entender
    @Override
    public void atualizar(long id, Consulta consulta) {
        try {
            String sql = "UPDATE consulta SET nome_medico=?, nome_paciente=?, " +
                         "data_consulta=?, diagnostico=?, status=? WHERE id=?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, consulta.getNomeMedico());
            comando.setString(2, consulta.getNomePaciente());
            // LocalDate precisa ser convertido pra java.sql.Date
            comando.setDate(3, java.sql.Date.valueOf(consulta.getDataConsulta()));
            comando.setString(4, consulta.getDiagnostico());
            comando.setString(5, consulta.getStatus());
            comando.setLong(6, id); // id no final por causa do WHERE

            comando.executeUpdate();
            System.out.println("Consulta atualizada");
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar consulta");
            erro.printStackTrace();
        }
    }

    // insere uma nova linha na tabela consulta
    // o id nao e passado pois e gerado automaticamente pelo AUTO_INCREMENT do banco
    // a ordem dos setString deve bater com a ordem dos campos no INSERT
    @Override
    public void cadastrar(Consulta consulta) {
        try {
            String sql = "INSERT INTO consulta (nome_medico, nome_paciente, data_consulta, diagnostico, status) " +
                         "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, consulta.getNomeMedico());
            comando.setString(2, consulta.getNomePaciente());
            comando.setDate(3, java.sql.Date.valueOf(consulta.getDataConsulta()));
            comando.setString(4, consulta.getDiagnostico());
            comando.setString(5, consulta.getStatus());

            comando.executeUpdate();
            System.out.println("Consulta cadastrada");
        } catch (SQLException erro) {
            System.out.println("Erro ao cadastrar consulta");
            erro.printStackTrace();
        }
    }

    // deleta a consulta usando o id como chave primaria
    // o objeto inteiro e passado mas so o id e usado
    @Override
    public void apagar(Consulta consulta) {
        try {
            String sql = "DELETE FROM consulta WHERE id = ?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setLong(1, consulta.getId());

            comando.executeUpdate();
            System.out.println("Consulta apagada");
        } catch (SQLException erro) {
            System.out.println("Erro ao apagar consulta");
            erro.printStackTrace();
        }
    }

    // busca consultas filtrando pelo nome do paciente
    // o % nas duas pontas do LIKE permite pesquisa parcial
    // passando string vazia retorna todas as consultas
    // para cada linha do banco, monta um objeto Consulta e adiciona na lista
    @Override
    public List<Consulta> pesquisarPorPaciente(String nomePaciente) {
        List<Consulta> listaConsultas = new ArrayList<>();
        try {
            String sql = "SELECT * FROM consulta WHERE nome_paciente LIKE ?";

            PreparedStatement comando = conexao.prepareStatement(sql);
            comando.setString(1, "%" + nomePaciente + "%");

            ResultSet registros = comando.executeQuery();

            while (registros.next()) {
                Consulta consulta = new Consulta();
                consulta.setId(registros.getLong("id"));
                consulta.setNomeMedico(registros.getString("nome_medico"));
                consulta.setNomePaciente(registros.getString("nome_paciente"));
                // converte java.sql.Date de volta pra LocalDate
                consulta.setDataConsulta(registros.getDate("data_consulta").toLocalDate());
                consulta.setDiagnostico(registros.getString("diagnostico"));
                consulta.setStatus(registros.getString("status"));
                listaConsultas.add(consulta);
            }
            System.out.println("Total de consultas carregadas: " + listaConsultas.size());
        } catch (SQLException erro) {
            System.out.println("Erro ao buscar consultas");
            erro.printStackTrace();
        }
        return listaConsultas;
    }
}
