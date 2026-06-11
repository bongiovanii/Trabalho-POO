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

// tela do CRUD de Consulta
// so cuida da parte visual - logica e validacao ficam no ConsultaControl
// monta o formulario, configura a tabela e trata os eventos dos botoes
public class ConsultaBoundary {

    private ConsultaControl control = new ConsultaControl();

    // control e instanciado aqui pra ja carregar os dados do banco ao abrir a tela

    // campos do formulario - ComboBox pra medico e paciente pois buscam do banco
    // DatePicker pra data pois facilita a selecao e evita erros de digitacao
    // TextField simples pro diagnostico pois e texto livre e opcional

    // tabela que exibe todas as consultas cadastradas
    // formatoData converte LocalDate pro padrao brasileiro dd/MM/yyyy
    private ComboBox<String> cbNomeMedico = new ComboBox<>();
    private ComboBox<String> cbNomePaciente = new ComboBox<>();
    private DatePicker dpDataConsulta = new DatePicker(LocalDate.now());
    private TextField txtDiagnostico  = new TextField();
    private ComboBox<String> cbStatus = new ComboBox<>();

    // tabela que exibe as consultas carregadas do banco
    private TableView<Consulta> tabela = new TableView<>();

    private DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Pane render() {

        // GridPane organiza os campos em linhas e colunas (label | campo)
        // BorderPane divide a tela: Top = formulario, Center = tabela
        GridPane painelCampos = new GridPane();
        painelCampos.setHgap(10);
        painelCampos.setVgap(8);

        // adiciona labels e campos no grid (coluna, linha)
        // carrega os nomes do banco ao montar a tela
        // se nao tiver medico ou paciente cadastrado, o ComboBox fica vazio
        painelCampos.add(new Label("Nome do Médico *:"),  0, 0);
        cbNomeMedico.setItems(control.carregarNomesMedicos());
        painelCampos.add(cbNomeMedico, 1, 0);

        painelCampos.add(new Label("Nome do Paciente *:"), 0, 1);
        cbNomePaciente.setItems(control.carregarNomesPacientes());
        painelCampos.add(cbNomePaciente, 1, 1);

        painelCampos.add(new Label("Data da Consulta * :"), 0, 2);
        painelCampos.add(dpDataConsulta, 1, 2);

        painelCampos.add(new Label("Diagnóstico (opcional):"), 0, 3);
        painelCampos.add(txtDiagnostico,  1, 3);

        painelCampos.add(new Label("Status *:"), 0, 4);
        cbStatus.getItems().addAll("Agendada", "Realizada", "Cancelada");
        painelCampos.add(cbStatus, 1, 4);

        // tres botoes: Salvar grava no banco, Novo limpa o formulario, Pesquisar filtra a tabela
        Button btnSalvar = new Button("Salvar");
        Button btnNovo = new Button("Novo");
        Button btnPesquisar = new Button("Pesquisar");

        painelCampos.add(btnSalvar, 0, 5);
        painelCampos.add(btnNovo, 1, 5);
        painelCampos.add(btnPesquisar, 2, 5);

        txtDiagnostico.setPromptText("Opcional");

        // BorderPane: Top = formulario, Center = tabela
        BorderPane painelPrincipal = new BorderPane();
        painelPrincipal.setTop(painelCampos);
        painelPrincipal.setCenter(tabela);

        // os ComboBox nao aceitam bindBidirectional direto com StringProperty
        // por isso listeners manuais nos dois sentidos:
        // - quando o usuario seleciona no ComboBox, atualiza a property do Control
        // - quando o Control limpa os campos, atualiza o ComboBox na tela
        // o TextField e o DatePicker aceitam bindBidirectional normalmente
        cbNomeMedico.valueProperty().addListener((obs, anterior, novo) -> control.nomeMedicoProperty().set(novo));
        control.nomeMedicoProperty().addListener((obs, anterior, novo) -> cbNomeMedico.setValue(novo));
        cbNomePaciente.valueProperty().addListener((obs, anterior, novo) -> control.nomePacienteProperty().set(novo));
        control.nomePacienteProperty().addListener((obs, anterior, novo) -> cbNomePaciente.setValue(novo));
        Bindings.bindBidirectional(txtDiagnostico.textProperty(), control.diagnosticoProperty());
        cbStatus.valueProperty().addListener((obs, anterior, novo) -> control.statusProperty().set(novo));
        control.statusProperty().addListener((obs, anterior, novo) -> cbStatus.setValue(novo));
        Bindings.bindBidirectional(dpDataConsulta.valueProperty(), control.dataConsultaProperty());

        // valida antes de salvar - se tiver erro mostra o Alert com a mensagem do campo
        // se tudo ok, chama salvar() no Control e mostra confirmacao
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

        // quando o usuario clica numa linha, fromEntity preenche o formulario com os dados
        // assim o usuario pode editar e salvar novamente
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

        // coluna de acoes tem um botao Apagar em cada linha
        // o callback define como cada celula dessa coluna e renderizada
        // celulas vazias (sem dados) nao exibem o botao
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

        // liga a tabela a ObservableList do Control
        // qualquer alteracao na lista (salvar, apagar) reflete automaticamente na tabela
        tabela.setItems(control.getLista());

        return painelPrincipal;
    }
}
