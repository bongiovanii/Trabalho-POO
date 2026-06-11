package edu.fatec.consulta.controls;

import java.time.LocalDate;

import edu.fatec.consulta.model.Consulta;
import edu.fatec.consulta.repository.ConsultaDAO;
import edu.fatec.consulta.repository.ConsultaDAOImplementation;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Control do CRUD de Consulta
// faz a ponte entre a tela (Boundary) e o banco (DAO)
// aqui fica toda a logica: validacao, conversao de dados e chamadas ao banco
public class ConsultaControl {

    // toda operacao de banco passa pelo dao
    private ConsultaDAO dao = new ConsultaDAOImplementation();

    // lista que alimenta o TableView - atualiza a tabela automaticamente quando muda
    private ObservableList<Consulta> listaConsultas = FXCollections.observableArrayList();

    // properties ligadas aos campos da tela via binding
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nomeMedico = new SimpleStringProperty("");
    private StringProperty nomePaciente = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataConsulta = new SimpleObjectProperty<>(LocalDate.now());
    private StringProperty diagnostico = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");

    public ConsultaControl() {
        carregar();
    }

    // monta um objeto Consulta com os valores atuais das properties
    // chamado antes de enviar os dados pro banco (salvar ou atualizar)
    // se o id for 0 e novo registro, se for maior que 0 e edicao
    public Consulta toEntity() {
        Consulta consulta = new Consulta();
        consulta.setId(id.get());
        consulta.setNomeMedico(nomeMedico.get());
        consulta.setNomePaciente(nomePaciente.get());
        consulta.setDataConsulta(dataConsulta.get());
        consulta.setDiagnostico(diagnostico.get());
        consulta.setStatus(status.get());
        return consulta;
    }
        
    // preenche as properties com os dados da consulta selecionada na tabela
    // quando o usuario clica numa linha, esse metodo e chamado automaticamente
    // pelo listener de selecao la na Boundary
    public void fromEntity(Consulta consulta) {
        if (consulta != null) {
            id.set(consulta.getId());
            nomeMedico.set(consulta.getNomeMedico());
            nomePaciente.set(consulta.getNomePaciente());
            dataConsulta.set(consulta.getDataConsulta());
            diagnostico.set(consulta.getDiagnostico());
            status.set(consulta.getStatus());
        }
    }

    // verifica os campos obrigatorios antes de salvar
    // retorna a mensagem do primeiro campo invalido que encontrar
    // se tudo estiver ok, retorna string vazia
    // a Boundary exibe essa mensagem num Alert de aviso
    public String validar() {
        if (nomePaciente.get().isBlank() || nomePaciente.get() == null){
            return "Preencha o campo Nome do Paciente *";
        }

        if (nomeMedico.get().isBlank() || nomeMedico.get() == null){
            return "Preencha o campo Nome do Médico *";
        }
            
        if (dataConsulta.get() == null){
            return "Preencha a Data da Consulta *";
        }
            
        if (dataConsulta.get().isBefore(LocalDate.of(1900, 1, 1))){
            return "Data da Consulta inválida.";
        }
            
        if (status.get() == null || status.get().isBlank()){
            return "Selecione o Status da Consulta *";
        }
            
        return "";
    }

    // reseta todas as properties pro valor padrao
    // chamado apos salvar ou quando o usuario clica no botao Novo
    // o id volta pra 0 indicando que o proximo salvar sera um INSERT e nao UPDATE
    public void limparCampos() {
        id.set(0);
        nomeMedico.set("");
        nomePaciente.set("");
        dataConsulta.set(LocalDate.now());
        diagnostico.set("");
        status.set("");
    }

    // decide se vai cadastrar ou atualizar baseado no id
    // id = 0 significa registro novo, id > 0 significa edicao
    // apos salvar, limpa o formulario e recarrega a lista da tabela
    public void salvar() {
        Consulta consulta = toEntity();
        if (id.get() > 0) {
            dao.atualizar(id.get(), consulta);
        } else {
            dao.cadastrar(consulta);
        }
        limparCampos();
        carregar();
    }

    // busca o objeto na lista pelo indice da linha selecionada na tabela
    // deleta no banco e recarrega a lista
    public void apagar(int indice) {
        Consulta consulta = listaConsultas.get(indice);
        dao.apagar(consulta);
        carregar();
    }

    // busca todas as consultas do banco e popula a lista
    // chamado no construtor pra ja ter dados quando a tela abrir
    // e chamado apos qualquer operacao de escrita pra manter a tabela atualizada
    public void carregar() {
        listaConsultas.clear();
        listaConsultas.addAll(dao.pesquisarPorPaciente(""));
    }

    // filtra a lista pelo nome do paciente digitado no campo de pesquisa
    // usa o mesmo metodo do DAO com LIKE, entao aceita texto parcial
    public void pesquisar() {
        listaConsultas.clear();
        listaConsultas.addAll(dao.pesquisarPorPaciente(getNomePaciente()));
    }

    // abre uma conexao separada e busca todos os nomes da tabela paciente
    // usado pra popular o ComboBox de paciente na tela
    // retorna lista vazia se nao tiver pacientes cadastrados ou der erro
    public ObservableList<String> carregarNomesPacientes() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        try {
            java.sql.Connection con = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false",
                "root", ""
            );
            java.sql.PreparedStatement stm = con.prepareStatement("SELECT nome FROM paciente");
            java.sql.ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao carregar pacientes");
            e.printStackTrace();
        }
        return nomes;
    }

    // mesma logica do carregarNomesPacientes, so que pra tabela medico
    // usado pra popular o ComboBox de medico na tela
    public ObservableList<String> carregarNomesMedicos() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        try {
            java.sql.Connection con = java.sql.DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/clinica?allowPublicKeyRetrieval=true&useSSL=false",
                "root", ""
            );
            java.sql.PreparedStatement stm = con.prepareStatement("SELECT nome FROM medico");
            java.sql.ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                nomes.add(rs.getString("nome"));
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Erro ao carregar medicos");
            e.printStackTrace();
        }
        return nomes;
    }

    public String getNomePaciente() { return nomePaciente.get(); }
    
    public StringProperty nomePacienteProperty() { return nomePaciente; }

    public StringProperty nomeMedicoProperty() { return nomeMedico; }

    public ObjectProperty<LocalDate> dataConsultaProperty() { return dataConsulta; }

    public StringProperty diagnosticoProperty() { return diagnostico; }

    public StringProperty statusProperty() { return status; }

    public ObservableList<Consulta> getLista() { return listaConsultas; }
}
