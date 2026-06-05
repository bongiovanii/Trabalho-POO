package edu.fatec.exame.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import edu.fatec.exame.model.Exame;
import edu.fatec.exame.controls.ExameControl;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

/**
 * Camada de visão (Boundary/View) do CRUD de Exame.
 *
 * Responsável por:
 *  - Montar o formulário com os campos de entrada
 *  - Fazer o binding bidirecional entre campos e Properties do Control
 *  - Configurar o TableView com suas colunas
 *  - Tratar eventos dos botões (salvar, pesquisar, apagar, novo)
 *
 * Não contém lógica de negócio: tudo é delegado ao ExameControl.
 */
public class ExameBoundary {

    // --- Campos do formulário ---
    private TextField txtTipo           = new TextField();
    private DatePicker dpDataRealizacao = new DatePicker(LocalDate.now());
    private TextField txtResultado      = new TextField();
    private TextField txtObservacao     = new TextField();
    private TextField txtNomePaciente   = new TextField();

    // Formatador de data para exibição no padrão brasileiro
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Control: camada intermediária entre a tela e o banco de dados
    private ExameControl control = new ExameControl();

    // Tabela que exibe a lista de exames carregados do banco
    private TableView<Exame> table = new TableView<>();

    /**
     * Monta e retorna o painel principal da tela de Exame.
     * Este método é chamado pela tela principal (PrincipalBoundary) para
     * exibir esta tela dentro da janela do aplicativo.
     */
    public Pane render() {

        // BorderPane divide a tela em regiões: Top (formulário) e Center (tabela)
        BorderPane panPrincipal = new BorderPane();

        // GridPane organiza os campos em linhas e colunas (label | campo)
        GridPane paneCampos = new GridPane();
        paneCampos.setHgap(10); // espaço horizontal entre colunas
        paneCampos.setVgap(8);  // espaço vertical entre linhas

        // --- Adição dos campos ao GridPane ---
        // Formato: add(componente, coluna, linha)
        paneCampos.add(new Label("Tipo do Exame:"),   0, 0);
        paneCampos.add(txtTipo,                        1, 0);

        paneCampos.add(new Label("Data de Realização:"), 0, 1);
        paneCampos.add(dpDataRealizacao,                  1, 1);

        paneCampos.add(new Label("Resultado:"),        0, 2);
        paneCampos.add(txtResultado,                   1, 2);

        paneCampos.add(new Label("Observação:"),       0, 3);
        paneCampos.add(txtObservacao,                  1, 3);

        paneCampos.add(new Label("Nome do Paciente:"), 0, 4);
        paneCampos.add(txtNomePaciente,                1, 4);

        // --- Botões ---
        Button btnSalvar    = new Button("Salvar");
        Button btnPesquisar = new Button("Pesquisar");
        Button btnNovo      = new Button("Novo");

        paneCampos.add(btnNovo,      0, 5);
        paneCampos.add(btnSalvar,    1, 5);
        paneCampos.add(btnPesquisar, 2, 5);

        panPrincipal.setTop(paneCampos);
        panPrincipal.setCenter(table);

        // --- Binding bidirecional ---
        // Liga os campos da tela às Properties do Control.
        // Quando o usuário digita no campo, a Property atualiza.
        // Quando o Control altera a Property (ex: limparCampos), o campo atualiza.
        Bindings.bindBidirectional(txtTipo.textProperty(),          control.tipoProperty());
        Bindings.bindBidirectional(txtResultado.textProperty(),     control.resultadoProperty());
        Bindings.bindBidirectional(txtObservacao.textProperty(),    control.observacaoProperty());
        Bindings.bindBidirectional(txtNomePaciente.textProperty(),  control.nomePacienteProperty());

        // Binding do DatePicker usa um converter para transformar LocalDate <-> String
        StringConverter<LocalDate> converter = new LocalDateStringConverter(dtf, dtf);
        Bindings.bindBidirectional(
            dpDataRealizacao.valueProperty(),
            control.dataRealizacaoProperty()
        );

        // --- Evento: Salvar ---
        // Valida os campos antes de salvar. Se houver erro, exibe Alert de aviso.
        // Se estiver ok, salva e exibe confirmação.
        btnSalvar.setOnAction(e -> {
            String erro = control.validar();
            if (!erro.isEmpty()) {
                // Exibe a mensagem de erro do campo específico (Critério 3 - Ideal)
                new Alert(AlertType.WARNING, erro).show();
            } else {
                control.salvar();
                new Alert(AlertType.INFORMATION, "Exame salvo com sucesso!").show();
            }
        });

        // --- Evento: Pesquisar ---
        // Filtra a tabela pelo tipo digitado no campo txtTipo
        btnPesquisar.setOnAction(e -> control.pesquisar());

        // --- Evento: Novo ---
        // Limpa todos os campos para cadastrar um novo exame
        btnNovo.setOnAction(e -> control.limparCampos());

        // --- Listener de seleção da tabela ---
        // Quando o usuário clica em uma linha, preenche o formulário com os dados
        // daquele exame para permitir edição.
        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, antigo, novo) -> control.fromEntity(novo)
        );

        // --- Configuração das colunas do TableView ---

        // Coluna "Tipo": exibe o tipo do exame
        TableColumn<Exame, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getTipo())
        );

        // Coluna "Data": exibe a data formatada como dd/MM/yyyy
        TableColumn<Exame, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(
                item.getValue().getDataRealizacao().format(dtf)
            )
        );

        // Coluna "Resultado"
        TableColumn<Exame, String> colResultado = new TableColumn<>("Resultado");
        colResultado.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getResultado())
        );

        // Coluna "Paciente"
        TableColumn<Exame, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNomePaciente())
        );

        // Coluna "Ações": contém o botão de apagar para cada linha
        TableColumn<Exame, Void> colAcoes = new TableColumn<>("Ações");

        // Callback define como cada célula da coluna de ações é renderizada
        Callback<TableColumn<Exame, Void>, TableCell<Exame, Void>> callback =
            new Callback<>() {
                public TableCell<Exame, Void> call(TableColumn<Exame, Void> column) {
                    return new TableCell<Exame, Void>() {

                        // Botão de apagar criado uma vez por célula
                        Button btnApagar = new Button("Apagar");

                        {
                            // Evento do botão: pede confirmação antes de apagar
                            btnApagar.setOnAction(e -> {
                                Alert alert = new Alert(
                                    AlertType.CONFIRMATION,
                                    "Deseja apagar este exame?",
                                    ButtonType.YES, ButtonType.NO
                                );
                                alert.setTitle("Confirmar exclusão");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.YES) {
                                    // getIndex() retorna a linha desta célula na tabela
                                    control.apagar(getIndex());
                                }
                            });
                        }

                        // updateItem é chamado pelo JavaFX para renderizar cada célula.
                        // Células vazias (empty=true) não devem exibir o botão.
                        @Override
                        public void updateItem(Void param, boolean empty) {
                            super.updateItem(param, empty);
                            setGraphic(empty ? null : btnApagar);
                        }
                    };
                }
            };

        colAcoes.setCellFactory(callback);

        // Adiciona todas as colunas à tabela
        table.getColumns().add(colTipo);
        table.getColumns().add(colData);
        table.getColumns().add(colResultado);
        table.getColumns().add(colPaciente);
        table.getColumns().add(colAcoes);

        // Liga a tabela à ObservableList do Control.
        // Qualquer alteração na lista (após salvar, apagar, etc.) reflete automaticamente.
        table.setItems(control.getLista());

        return panPrincipal;
    }
}
