package edu.fatec.consulta.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import edu.fatec.consulta.controls.ConsultaControl;
import edu.fatec.consulta.model.Consulta;
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

// Tela do CRUD de Consulta
public class ConsultaBoundary {

    private ConsultaControl control = new ConsultaControl();

    // campos do formulario
    private TextField txtNomeMedico = new TextField();
    private TextField txtNomePaciente = new TextField();
    private DatePicker dpDataConsulta = new DatePicker(LocalDate.now());
    private TextField txtDiagnostico  = new TextField();
    private ComboBox<String> cbStatus = new ComboBox<>();

    // tabela que exibe as consultas carregadas do banco
    private TableView<Consulta> tabela = new TableView<>();

    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pane render() {

        GridPane painelCampos = new GridPane();
        painelCampos.setHgap(10);
        painelCampos.setVgap(8);

        // adiciona labels e campos no grid (coluna, linha)
        painelCampos.add(new Label("Nome do Médico:"),  0, 0);
        painelCampos.add(txtNomeMedico, 1, 0);

        painelCampos.add(new Label("Nome do Paciente:"), 0, 1);
        painelCampos.add(txtNomePaciente, 1, 1);

        painelCampos.add(new Label("Data da Consulta:"), 0, 2);
        painelCampos.add(dpDataConsulta, 1, 2);

        painelCampos.add(new Label("Diagnóstico:"), 0, 3);
        painelCampos.add(txtDiagnostico,  1, 3);

        cbStatus.getItems().addAll("Agendada", "Realizada", "Cancelada");
        painelCampos.add(new Label("Status:"), 0, 4);
        painelCampos.add(cbStatus, 1, 4);

        Button btnSalvar = new Button("Salvar");
        Button btnNovo = new Button("Novo");
        Button btnPesquisar = new Button("Pesquisar");

        painelCampos.add(btnSalvar, 0, 5);
        painelCampos.add(btnNovo, 1, 5);
        painelCampos.add(btnPesquisar, 2, 5);

        // BorderPane: Top = formulario, Center = tabela
        BorderPane painelPrincipal = new BorderPane();
        painelPrincipal.setTop(painelCampos);
        painelPrincipal.setCenter(tabela);

        // binding bidirecional - campo e property ficam sincronizados
        Bindings.bindBidirectional(txtNomeMedico.textProperty(), control.nomeMedicoProperty());
        Bindings.bindBidirectional(txtNomePaciente.textProperty(), control.nomePacienteProperty());
        Bindings.bindBidirectional(txtDiagnostico.textProperty(), control.diagnosticoProperty());
        Bindings.bindBidirectional(cbStatus.valueProperty(), control.statusProperty());
        Bindings.bindBidirectional(dpDataConsulta.valueProperty(), control.dataConsultaProperty());

        // botao salvar valida antes de gravar
        btnSalvar.setOnAction(e -> {
            String mensagemErro = control.validar();
            if (!mensagemErro.isEmpty()) {
                new Alert(AlertType.WARNING, mensagemErro).show();
            } else {
                control.salvar();
                new Alert(AlertType.INFORMATION, "Consulta salva com sucesso!").show();
            }
        });

        btnNovo.setOnAction(e -> control.limparCampos());

        btnPesquisar.setOnAction(e -> control.pesquisar());

        // quando clica numa linha, preenche o formulario pra edicao
        tabela.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, selecionado) -> control.fromEntity(selecionado)
        );

        // colunas da tabela
        TableColumn<Consulta, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNomeMedico())
        );

        TableColumn<Consulta, String> colPaciente = new TableColumn<>("Paciente");
        colPaciente.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getNomePaciente())
        );

        TableColumn<Consulta, String> colData = new TableColumn<>("Data");
        colData.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(
                item.getValue().getDataConsulta().format(formatoData)
            )
        );

        TableColumn<Consulta, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(
            item -> new ReadOnlyStringWrapper(item.getValue().getStatus())
        );

        TableColumn<Consulta, Void> colAcoes = new TableColumn<>("Ações");

        Callback<TableColumn<Consulta, Void>, TableCell<Consulta, Void>> callback =
            new Callback<>() {
                public TableCell<Consulta, Void> call(TableColumn<Consulta, Void> coluna) {
                    return new TableCell<Consulta, Void>() {

                        Button btnApagar = new Button("Apagar");

                        {
                            btnApagar.setOnAction(e -> {
                                Alert confirmacao = new Alert(
                                    AlertType.CONFIRMATION,
                                    "Deseja apagar esta consulta?",
                                    ButtonType.YES, ButtonType.NO
                                );
                                confirmacao.setTitle("Confirmar exclusão");

                                Optional<ButtonType> resposta = confirmacao.showAndWait();
                                if (resposta.isPresent() && resposta.get() == ButtonType.YES) {
                                    control.apagar(getIndex());
                                }
                            });
                        }

                        @Override
                        public void updateItem(Void param, boolean vazio) {
                            super.updateItem(param, vazio);
                            // so mostra o botao em celulas com dados
                            setGraphic(vazio ? null : btnApagar);
                        }
                    };
                }
            };

        colAcoes.setCellFactory(callback);

        tabela.getColumns().add(colMedico);
        tabela.getColumns().add(colPaciente);
        tabela.getColumns().add(colData);
        tabela.getColumns().add(colStatus);
        tabela.getColumns().add(colAcoes);

        // liga a tabela a lista do control
        tabela.setItems(control.getLista());

        return painelPrincipal;
    }
}
