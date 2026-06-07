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

// faz a ponte entre a tela (Boundary) e o banco (DAO)
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

    // preenche os campos quando o usuario clica numa linha da tabela
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

    // valida os campos obrigatorios - retorna mensagem do campo com erro
    public String validar() {
        if (nomePaciente.get().isBlank())
            return "Preencha o campo Nome do Paciente.";
        if (nomeMedico.get().isBlank())
            return "Preencha o campo Nome do Médico.";
        if (status.get().isBlank())
            return "Preencha o campo Status (ex: Agendada, Realizada, Cancelada).";
        return "";
    }

    // limpa o formulario pra cadastrar uma nova consulta
    public void limparCampos() {
        id.set(0);
        nomeMedico.set("");
        nomePaciente.set("");
        dataConsulta.set(LocalDate.now());
        diagnostico.set("");
        status.set("");
    }

    // salva ou atualiza dependendo se ja tem id
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

    // apaga a consulta da linha selecionada na tabela
    public void apagar(int indice) {
        Consulta consulta = listaConsultas.get(indice);
        dao.apagar(consulta);
        carregar();
    }

    // recarrega a lista do banco
    public void carregar() {
        listaConsultas.clear();
        listaConsultas.addAll(dao.pesquisarPorPaciente(""));
    }

    // filtra a tabela pelo nome do paciente digitado
    public void pesquisar() {
        listaConsultas.clear();
        listaConsultas.addAll(dao.pesquisarPorPaciente(getNomePaciente()));
    }

    public String getNomePaciente() { return nomePaciente.get(); }
    
    public StringProperty nomePacienteProperty() { return nomePaciente; }

    public StringProperty nomeMedicoProperty() { return nomeMedico; }

    public ObjectProperty<LocalDate> dataConsultaProperty() { return dataConsulta; }

    public StringProperty diagnosticoProperty() { return diagnostico; }

    public StringProperty statusProperty() { return status; }

    public ObservableList<Consulta> getLista() { return listaConsultas; }
}
