package edu.fatec.paciente.controls;

import java.time.LocalDate;

import edu.fatec.paciente.model.Paciente;
import edu.fatec.paciente.repository.PacienteDAOImplementation;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import edu.fatec.paciente.repository.PacienteDAO;

// Control CRUD de Paciente
// faz a ponte entre a tela (Boundary) e o banco (DAO)
public class PacienteControl {

    
    // lista que alimenta o TableView - quando muda, a tabela atualiza sozinha
    private ObservableList<Paciente> lista = FXCollections.observableArrayList();

    // properties ligadas aos campos da tela via binding
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private StringProperty cpf = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");
    private StringProperty email = new SimpleStringProperty("");
    private StringProperty endereco = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataNascimento = new SimpleObjectProperty<>(LocalDate.of(1900, 1, 1));
    private DoubleProperty peso = new SimpleDoubleProperty(0.0);
    private DoubleProperty altura = new SimpleDoubleProperty(0.0);

    private PacienteDAO dao = new PacienteDAOImplementation();
    
    public PacienteControl() {
        carregar();
    }

    private void carregar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(""));
    }

      // preenche os campos quando o usuario clica numa linha da tabela
    public void fromEntity(Paciente p) {
        if (p != null) {
            id.set(p.getId());
            nome.set(p.getNome());
            cpf.set(p.getCpf());
            telefone.set(p.getTelefone());
            email.set(p.getEmail());
            endereco.set(p.getEndereco());
            dataNascimento.set(p.getDataNascimento());
            peso.set(p.getPeso());
            altura.set(p.getAltura());
        }
    }

    
    // monta o objeto Exame com os valores atuais dos campos
    public Paciente toEntity() {
        Paciente p = new Paciente();
        p.setId(id.get());
        p.setNome(nome.get());
        p.setCpf(cpf.get());
        p.setTelefone(telefone.get());
        p.setEmail(email.get());
        p.setEndereco(endereco.get());
        p.setDataNascimento(dataNascimento.get());
        p.setPeso(peso.get());
        p.setAltura(altura.get());
        return p;
    }


       // limpa o formulario pra cadastrar um novo
    public void limparCampos() {
        id.set(0);
        nome.set("");
        cpf.set("");
        telefone.set("");
        email.set("");
        endereco.set("");
        dataNascimento.set(LocalDate.of(1900, 1, 1));
        peso.set(0.0);
        altura.set(0.0);
    }

    // valida os campos obrigatorios - retorna mensagem do campo com erro
    public String validar() {
        if (nome.get().isBlank()) {
            return "Preencha o campo Nome";
        }
        if (cpf.get().isBlank()) {
            return "Preencha o campo CPF";
        }
        return "";
    }

    // salva ou atualiza dependendo se ja tem id
    public void salvar() {
       Paciente p = toEntity();
        if (id.get() > 0) {
            // ID preenchido = registro já existe → atualiza
            dao.atualizar(id.get(), p);
        } else {
            // ID zerado = novo registro → cadastra
            dao.inserir(p);
        }
        limparCampos();
        carregar();
        
    }

    // apaga o exame da linha selecionada na tabela
    public void excluir( int indice) {
        Paciente p = lista.get(indice);
        dao.excluir(p);
        limparCampos();
        carregar();
    }


    // filtra a tabela pelo nome digitado
    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(getNome()));
    }

    public String getNome() { return nome.get(); }


    public ObservableList<Paciente> getLista() { return lista; }
    public StringProperty nomeProperty() { return nome; }
    public StringProperty cpfProperty() { return cpf; }
    public StringProperty telefoneProperty() { return telefone; }
    public StringProperty emailProperty() { return email; }
    public StringProperty enderecoProperty() { return endereco; }
    public ObjectProperty<LocalDate> dataNascimentoProperty() { return dataNascimento; }
    public DoubleProperty pesoProperty() { return peso; }
    public DoubleProperty alturaProperty() { return altura; }





}
