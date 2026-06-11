package edu.fatec.exame.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import edu.fatec.exame.controls.ExameControl;
import edu.fatec.exame.model.Exame;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class ExameBoundary {

    // campos do formulário
    private TextField txtTipo = new TextField();
    private DatePicker dpDataRealizacao = new DatePicker(LocalDate.now());
    private TextField txtResultado = new TextField();
    private TextField txtObservacao = new TextField();
    private ComboBox<String> cbNomePaciente = new ComboBox<>();
    private ComboBox<String> cbStatus = new ComboBox<>();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ExameControl control = new ExameControl();

    private TableView<Exame> table = new TableView<>();

    // método que monta a interface gráfica e retorna o painel principal
    public Pane render() {

        // borderPane divide a tela em regiões: Top (formulário) e Center (tabela)
        BorderPane painelPrincipal = new BorderPane();

        // gridPane organiza os campos em linhas e colunas (label | campo)
        GridPane painelCampos = new GridPane();
        painelCampos.setHgap(10); // espaço horizontal entre colunas
        painelCampos.setVgap(8);  // espaço vertical entre linhas

        // adição dos campos ao GridPane
        painelCampos.add(new Label("Tipo do Exame *:"), 0, 0);
        painelCampos.add(txtTipo, 1, 0);

        painelCampos.add(new Label("Data de Realização *:"), 0, 1);
        painelCampos.add(dpDataRealizacao, 1, 1);

        painelCampos.add(new Label("Resultado (opcional):"), 0, 2);
        painelCampos.add(txtResultado, 1, 2);

        painelCampos.add(new Label("Observação (opcional):"), 0, 3);
        painelCampos.add(txtObservacao, 1, 3);

        painelCampos.add(new Label("Status *:"), 0, 4);
        cbStatus.getItems().addAll("Solicitado", "Aguardando resultado", "Concluido");
        painelCampos.add(cbStatus, 1, 4);

        painelCampos.add(new Label("Paciente *:"), 0, 5); 
        cbNomePaciente.setItems(control.carregarNomesPacientes());
        painelCampos.add(cbNomePaciente, 1, 5);

        // botões
        Button btnSalvar = new Button("Salvar");
        Button btnPesquisar = new Button("Pesquisar");
        Button btnNovo = new Button("Novo");

        painelCampos.add(btnNovo, 0, 6);
        painelCampos.add(btnSalvar, 1, 6);
        painelCampos.add(btnPesquisar, 2, 6);

        painelPrincipal.setTop(painelCampos);
        painelPrincipal.setCenter(table);

        // bidings direcional - campo e property ficam sincornizadas
        Bindings.bindBidirectional(txtTipo.textProperty(), control.tipoProperty());
        Bindings.bindBidirectional(txtResultado.textProperty(), control.resultadoProperty());
        Bindings.bindBidirectional(txtObservacao.textProperty(), control.observacaoProperty());
        cbNomePaciente.valueProperty().addListener((obs, anterior, novo) -> control.nomePacienteProperty().set(novo));
        control.nomePacienteProperty().addListener((obs, anterior, novo) -> cbNomePaciente.setValue(novo));
        cbStatus.valueProperty().addListener((obs, anterior, novo) -> control.statusProperty().set(novo));
        control.statusProperty().addListener((obs, anterior, novo) -> cbStatus.setValue(novo));        Bindings.bindBidirectional(cbStatus.valueProperty(), control.statusProperty());

        txtTipo.setPromptText("Obrigatorio");
        txtResultado.setPromptText("Opcional");
        txtObservacao.setPromptText("Opcional");

        // botao novo limpa o formulario
        btnNovo.setOnAction(e -> control.limparCampos());

        // botao salvar valida antes de gravar
        btnSalvar.setOnAction(e -> {
            String mensagemErro = control.validar();
            if (!mensagemErro.isEmpty()) {
                new Alert(AlertType.WARNING, mensagemErro).show();
            } else {
                control.salvar();
                new Alert(AlertType.INFORMATION, "Exame salvo com sucesso!").show();
            }
        });

        // botao pesquisar pelo tipo 
        btnPesquisar.setOnAction(e -> control.pesquisar());

        // botao novo limpa o formulario
        btnNovo.setOnAction(e -> control.limparCampos());

        // quando clica numa linha, preenche o formulario pra edicao
        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, selecionado) -> control.fromEntity(selecionado)
        );

        // colunas da tabela
        TableColumn<Exame, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getTipo())
        );

        TableColumn<Exame, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(
                item.getValue().getDataRealizacao().format(dtf)
            )
        );

        TableColumn<Exame, String> colResultado = new TableColumn<>("Resultado");
        colResultado.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getResultado())
        );

        TableColumn<Exame, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNomePaciente())
        );

        TableColumn<Exame, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getStatus())
        );
        table.getColumns().add(colStatus);

        TableColumn<Exame, Void> colAcoes = new TableColumn<>("Ações");

        // callback define como cada célula da coluna de ações é renderizada
        Callback<TableColumn<Exame, Void>, TableCell<Exame, Void>> callback =
            new Callback<>() {
                public TableCell<Exame, Void> call(TableColumn<Exame, Void> column) {
                    return new TableCell<Exame, Void>() {

                        Button btnApagar = new Button("Apagar");

                        {
                            btnApagar.setOnAction(e -> {
                                Alert alert = new Alert(
                                    AlertType.CONFIRMATION,
                                    "Deseja apagar este exame?",
                                    ButtonType.YES, ButtonType.NO
                                );
                                // Eu corrigi o título aqui também para o português correto com acento
                                alert.setTitle("Confirmar exclusão");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.YES) {
                                    control.apagar(getIndex());
                                }
                            });
                        }

                        // updateItem é chamado pelo JavaFX para renderizar cada célula
                        @Override
                        public void updateItem(Void param, boolean empty) {
                            super.updateItem(param, empty);
                            setGraphic(empty ? null : btnApagar);
                        }
                    };
                }
            };

        colAcoes.setCellFactory(callback);

        table.getColumns().add(colTipo);
        table.getColumns().add(colData);
        table.getColumns().add(colResultado);
        table.getColumns().add(colPaciente);
        table.getColumns().add(colAcoes);

        // liga a tabela à lista de exames
        table.setItems(control.getLista());

        return painelPrincipal;
    }
}
