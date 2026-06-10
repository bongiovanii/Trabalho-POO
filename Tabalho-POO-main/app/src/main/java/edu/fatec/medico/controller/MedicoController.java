package edu.fatec.medico.controller;

import edu.fatec.medico.model.Medico;
import edu.fatec.medico.repository.MedicoDAO;
import edu.fatec.medico.repository.MedicoDAOImplementation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.*;

public class MedicoController {
    // lista que vai alimentar o tableview
    private ObservableList<Medico> lista = FXCollections.observableArrayList();

    // nessa parte são as properties que vão ser ligadas aos campos da tela vida binding direcional

    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nome = new SimpleStringProperty("");
    private LongProperty crm = new SimpleLongProperty(0);
    private LongProperty cpf = new SimpleLongProperty(0);
    private StringProperty especialidade = new SimpleStringProperty("");
    private StringProperty telefone = new SimpleStringProperty("");

    // e agora eu crio as funções que vão retornar os valores das properties, essas funções são usadas para fazer o binding com os campos da tela

    public ObservableList<Medico> getLista(){return lista;}
    public LongProperty idProperty(){return id;}
    public StringProperty nomeProperty(){return nome;}
    public LongProperty crmProperty(){return crm;}
    public LongProperty cpfProperty(){return cpf;}
    public StringProperty especialidadeProperty(){return especialidade;}
    public StringProperty telefoneProperty(){return telefone;}

    private MedicoDAO dao =  new MedicoDAOImplementation();

    // O construtor agora chama o carregar() para que a lista de médicos apareça assim que a tela abrir
    public MedicoController() {
        carregar();
    }

    public void limparCampos(){
        id.set(0);
        nome.set("");
        crm.set(0);
        cpf.set(0);
        especialidade.set("");
        telefone.set("");
    }

    public String validar(){
        if (nome.get().isBlank()){
            return "O campo nome é obrigatório";
        }
        
        String sCrm = String.valueOf(crm.get());
        if (crm.get() <= 0 || sCrm.length() < 4 || sCrm.length() > 10) {
            return "O campo CRM deve ter entre 4 e 10 digitos";
        }

        String sCpf = String.valueOf(cpf.get());
        if (cpf.get() <= 0 || sCpf.length() != 11) {
            return "O campo CPF deve ter exatamente 11 digitos numericos";
        }

        if (especialidade.get().isBlank()){
            return "O campo especialidade é obrigatório";
        }

        return "";

    }

    public Medico toEntity(){
        // Cria/instancia um objeto médico
        Medico m = new Medico();
        m.setId(id.get());
        m.setNome(nome.get());
        m.setCrm(crm.get());
        m.setCpf(cpf.get());
        m.setEspecialidade(especialidade.get());
        m.setTelefone(telefone.get());
        return m;
    }

    public void fromEntity(Medico m){
        if(m != null){
            id.set(m.getId());
            nome.set(m.getNome());
            cpf.set(m.getCpf());
            crm.set(m.getCrm());
            especialidade.set(m.getEspecialidade());
            telefone.set(m.getTelefone());
        }
    }

    public void salvar(){
        Medico m =  toEntity();
        if(id.get() > 0){
            // se o id for maior que zero, significa que o médico já existe e deve ser atualizado
            dao.atualizar(id.get(), m);
        } else {
            // se o id for zero, significa que é um novo médico
            dao.inserir(m);
        }
        limparCampos();
        carregar();
    }

    public void pesquisar(){
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(getNome()));
    }

    public String getNome() {
        return nome.get();
    }

    // Deixei público para poder ser chamado se necessário, e agora limpa e recarrega todos os médicos
    public void carregar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorNome(""));
    }

    public void excluir(int indice){
        Medico m = lista.get(indice);
        dao.excluir(m);
        limparCampos();
        carregar();
    }

}
