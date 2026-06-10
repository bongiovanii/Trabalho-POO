package edu.fatec.exame.controls;

import java.time.LocalDate;

import edu.fatec.exame.model.Exame;
import edu.fatec.exame.repository.ExameDAO;
import edu.fatec.exame.repository.ExameDAOImplementation;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Control do CRUD de Exame
// faz a ponte entre a tela (Boundary) e o banco (DAO)
public class ExameControl {

    // lista que alimenta o TableView - quando muda, a tabela atualiza sozinha
    private ObservableList<Exame> lista = FXCollections.observableArrayList();

    // properties ligadas aos campos da tela via binding
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty tipo = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataRealizacao = new SimpleObjectProperty<>(LocalDate.now());
    private StringProperty resultado = new SimpleStringProperty("");
    private StringProperty observacao = new SimpleStringProperty("");
    private StringProperty nomePaciente = new SimpleStringProperty("");

    // toda operacao de banco passa pelo dao
    private ExameDAO dao = new ExameDAOImplementation();

    // ja carrega os exames ao abrir a tela
    public ExameControl() {
        carregar();
    }

    // preenche os campos quando o usuario clica numa linha da tabela
    public void fromEntity(Exame exame) {
        if (exame != null) {
            id.set(exame.getId());
            tipo.set(exame.getTipo());
            dataRealizacao.set(exame.getDataRealizacao());
            resultado.set(exame.getResultado());
            observacao.set(exame.getObservacao());
            nomePaciente.set(exame.getNomePaciente());
        }
    }

    // monta o objeto Exame com os valores atuais dos campos
    public Exame toEntity() {
        Exame exame = new Exame();
        exame.setId(id.get());
        exame.setTipo(tipo.get());
        exame.setDataRealizacao(dataRealizacao.get());
        exame.setResultado(resultado.get());
        exame.setObservacao(observacao.get());
        exame.setNomePaciente(nomePaciente.get());
        return exame;
    }

    // limpa o formulario pra cadastrar um novo
    public void limparCampos() {
        id.set(0);
        tipo.set("");
        dataRealizacao.set(LocalDate.now());
        resultado.set("");
        observacao.set("");
        nomePaciente.set("");
    }

    // valida os campos obrigatorios - retorna mensagem do campo com erro
    public String validar() {
        if (tipo.get().isBlank()){
            return "Preencha o campo Tipo do Exame.";
        }
            
        if (tipo.get().length() < 3){
            return "Tipo do Exame deve ter ao menos 3 caracteres.";
        }
            
        if (dataRealizacao.get() == null){
            return "Preencha a Data de Realização.";
        }
            
        if (dataRealizacao.get().isAfter(LocalDate.now())){
            return "A data de realização não pode ser no futuro.";
        }
            
        if (resultado.get().isBlank()){
            return "Preencha o campo Resultado.";
        }
            
        if (resultado.get().length() < 3){
            return "Resultado deve ter ao menos 3 caracteres.";
        }
            
        if (nomePaciente.get().isBlank()){
            return "Preencha o campo Nome do Paciente.";
        }
            
        if (nomePaciente.get().length() < 3){
            return "Nome do Paciente deve ter ao menos 3 caracteres.";
        }
            
        return "";
    }

    // salva ou atualiza dependendo se ja tem id
    public void salvar() {
        Exame exame = toEntity();
        if (id.get() > 0) {
            // ID preenchido = registro já existe → atualiza
            dao.atualizar(id.get(), exame);
        } else {
            // ID zerado = novo registro → cadastra
            dao.cadastrar(exame);
        }
        limparCampos();
        carregar();
    }

    // recarrega a lista do banco
    public void carregar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorTipo(""));
    }

    // apaga o exame da linha selecionada na tabela
    public void apagar(int indice) {
        Exame exame = lista.get(indice);
        dao.apagar(exame);
        carregar();
    }

    // filtra a tabela pelo tipo digitado
    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorTipo(getTipo()));
    }

    public String getTipo() { return tipo.get(); }
    
    public StringProperty tipoProperty() { return tipo; }

    public ObjectProperty<LocalDate> dataRealizacaoProperty() { return dataRealizacao; }

    public StringProperty resultadoProperty() { return resultado; }

    public StringProperty observacaoProperty() { return observacao; }

    public StringProperty nomePacienteProperty() { return nomePaciente; }

    public ObservableList<Exame> getLista() { return lista; }
}
