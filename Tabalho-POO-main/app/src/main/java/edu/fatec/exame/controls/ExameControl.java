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

/**
 * Camada de controle (Control) do CRUD de Exame.
 *
 * Responsabilidades:
 *  - Manter as Properties JavaFX que ficam ligadas (binding) aos campos da tela
 *  - Converter entre objeto Exame (entidade) e Properties (estado da tela)
 *  - Chamar o DAO para operações de banco de dados
 *  - Manter a lista observável que alimenta o TableView
 */
public class ExameControl {

    // Lista observável: quando alterada, o TableView atualiza automaticamente na tela.
    private ObservableList<Exame> lista = FXCollections.observableArrayList();

    // --- Properties JavaFX ---
    // Cada campo do formulário na tela fica "ligado" (bound) a uma dessas properties.
    // Quando o usuário digita, a property atualiza; quando a property muda, a tela atualiza.
    private LongProperty id                        = new SimpleLongProperty(0);
    private StringProperty tipo                    = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> dataRealizacao = new SimpleObjectProperty<>(LocalDate.now());
    private StringProperty resultado               = new SimpleStringProperty("");
    private StringProperty observacao              = new SimpleStringProperty("");
    private StringProperty nomePaciente            = new SimpleStringProperty("");

    // Instância do DAO: toda comunicação com o banco passa por aqui.
    private ExameDAO dao = new ExameDAOImplementation();

    /**
     * Construtor: ao criar o Control, já carrega os exames do banco
     * para popular o TableView assim que a tela abrir.
     */
    public ExameControl() {
        carregar();
    }

    /**
     * Preenche as Properties com os dados de um objeto Exame.
     * Chamado quando o usuário clica em uma linha do TableView para editar.
     * @param e exame selecionado na tabela (pode ser null se nada selecionado)
     */
    public void fromEntity(Exame e) {
        if (e != null) {
            id.set(e.getId());
            tipo.set(e.getTipo());
            dataRealizacao.set(e.getDataRealizacao());
            resultado.set(e.getResultado());
            observacao.set(e.getObservacao());
            nomePaciente.set(e.getNomePaciente());
        }
    }

    /**
     * Cria um objeto Exame a partir dos valores atuais das Properties.
     * Chamado antes de enviar os dados para o DAO (salvar ou atualizar).
     * @return objeto Exame preenchido com os dados do formulário
     */
    public Exame toEntity() {
        Exame e = new Exame();
        e.setId(id.get());
        e.setTipo(tipo.get());
        e.setDataRealizacao(dataRealizacao.get());
        e.setResultado(resultado.get());
        e.setObservacao(observacao.get());
        e.setNomePaciente(nomePaciente.get());
        return e;
    }

    /**
     * Reseta todas as Properties para os valores padrão.
     * Chamado após salvar ou quando o usuário clica em "Novo".
     */
    public void limparCampos() {
        id.set(0);
        tipo.set("");
        dataRealizacao.set(LocalDate.now());
        resultado.set("");
        observacao.set("");
        nomePaciente.set("");
    }

    /**
     * Valida os campos obrigatórios antes de salvar.
     * Retorna uma mensagem de erro, ou string vazia se tudo estiver ok.
     * Assim a Boundary pode exibir o Alert com a mensagem correta por campo.
     */
    public String validar() {
        if (tipo.get().isBlank())
            return "O campo 'Tipo' é obrigatório.";
        if (nomePaciente.get().isBlank())
            return "O campo 'Nome do Paciente' é obrigatório.";
        if (resultado.get().isBlank())
            return "O campo 'Resultado' é obrigatório.";
        return ""; // sem erros
    }

    /**
     * Salva ou atualiza um exame no banco.
     * Se o ID for 0, é um novo registro (INSERT); caso contrário, é edição (UPDATE).
     * Após salvar, limpa o formulário e recarrega a lista.
     */
    public void salvar() {
        Exame e = toEntity();
        if (id.get() > 0) {
            // ID preenchido = registro já existe → atualiza
            dao.atualizar(id.get(), e);
        } else {
            // ID zerado = novo registro → cadastra
            dao.cadastrar(e);
        }
        limparCampos();
        carregar();
    }

    /**
     * Recarrega a lista com todos os exames do banco.
     * Chamado no construtor e após qualquer operação de escrita.
     */
    public void carregar() {
        lista.clear();
        // Passando "" traz todos os registros (LIKE '%')
        lista.addAll(dao.pesquisarPorTipo(""));
    }

    /**
     * Remove o exame na posição informada da lista.
     * O índice vem da linha selecionada no TableView.
     * @param indice posição do exame na ObservableList
     */
    public void apagar(int indice) {
        Exame e = lista.get(indice);
        dao.apagar(e);
        carregar();
    }

    /**
     * Filtra a lista pelo tipo digitado no campo de pesquisa.
     */
    public void pesquisar() {
        lista.clear();
        lista.addAll(dao.pesquisarPorTipo(getTipo()));
    }

    // --- Getters de valor e de Property ---
    // O getter de valor (ex: getTipo()) é usado em lógicas internas.
    // O getter de Property (ex: tipoProperty()) é usado pelo binding na Boundary.

    public String getTipo() { return tipo.get(); }
    public StringProperty tipoProperty() { return tipo; }

    public ObjectProperty<LocalDate> dataRealizacaoProperty() { return dataRealizacao; }

    public StringProperty resultadoProperty() { return resultado; }

    public StringProperty observacaoProperty() { return observacao; }

    public StringProperty nomePacienteProperty() { return nomePaciente; }

    public ObservableList<Exame> getLista() { return lista; }
}
