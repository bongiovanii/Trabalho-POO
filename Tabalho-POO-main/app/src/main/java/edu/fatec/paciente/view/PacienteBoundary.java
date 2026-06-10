package edu.fatec.paciente.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import edu.fatec.paciente.controls.PacienteControl;
import edu.fatec.paciente.model.Paciente;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

public class PacienteBoundary {
    private PacienteControl control = new PacienteControl();
    private TextField id = new TextField();
    private TextField nome = new TextField();
    private TextField cpf = new TextField();
    private TextField telefone = new TextField();
    private TextField email = new TextField();
    private TextField endereco = new TextField();
    private DatePicker dataNascimento = new DatePicker(LocalDate.now());
    private TextField peso = new TextField();
    private TextField altura = new TextField();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private TableView<Paciente> table = new TableView<>();

    // método que monta a interface gráfica e retorna o painel principal
    public Pane render() {

        // borderPane divide a tela em regiões: Top (formulário) e Center (tabela)
        BorderPane painelPrincipal = new BorderPane();

        // gridPane organiza os campos em linhas e colunas (label | campo)
        GridPane painelCampos = new GridPane();
        painelCampos.setHgap(10); // seta o alinhamento horizontal entre colunas
        painelCampos.setVgap(8); // seta o alinhamento vertical entre linhas

        // Adição dos campos ao GridPane por (coluna, linha)
        painelCampos.add(new Label("ID:"), 0, 0);
        painelCampos.add(id, 1, 0);

        painelCampos.add(new Label("Nome:"), 0, 1);
        painelCampos.add(nome, 1, 1);

        painelCampos.add(new Label("CPF:"), 0, 2);
        painelCampos.add(cpf, 1, 2);

        painelCampos.add(new Label("Telefone:"), 0, 3);
        painelCampos.add(telefone, 1, 3);

        painelCampos.add(new Label("Email:"), 0, 4);
        painelCampos.add(email, 1, 4);

        painelCampos.add(new Label("Endereço:"), 0, 5);
        painelCampos.add(endereco, 1, 5);

        painelCampos.add(new Label("Data de Nascimento:"), 0, 6);
        painelCampos.add(dataNascimento, 1, 6);

        painelCampos.add(new Label("Peso:"), 0, 7);
        painelCampos.add(peso, 1, 7);

        painelCampos.add(new Label("Altura:"), 0, 8);
        painelCampos.add(altura, 1, 8);

        // Botões e ações de clique Salvar, Pesquisar e Excluir
        Button btnSalvar = new Button("Salvar");
        Button btnPesquisar = new Button("Pesquisar");
        Button btnExcluir = new Button("Excluir");
        Button btnNovo = new Button("Novo");

        // Adiciona botões ao painel de campos
        painelCampos.add(btnNovo, 0, 9);
        painelCampos.add(btnSalvar, 1, 9);
        painelCampos.add(btnPesquisar, 2, 9);
        painelCampos.add(btnExcluir, 3, 9);

        // IMPORTANTE: Adicionar o painel de campos e a tabela ao painel principal
        painelPrincipal.setTop(painelCampos);
        painelPrincipal.setCenter(table);

        // Bindings bidirecionais - campo e property ficam sincronizadas
        javafx.beans.binding.Bindings.bindBidirectional(nome.textProperty(), control.nomeProperty());
        javafx.beans.binding.Bindings.bindBidirectional(cpf.textProperty(), control.cpfProperty());
        javafx.beans.binding.Bindings.bindBidirectional(telefone.textProperty(), control.telefoneProperty());
        javafx.beans.binding.Bindings.bindBidirectional(email.textProperty(), control.emailProperty());
        javafx.beans.binding.Bindings.bindBidirectional(endereco.textProperty(), control.enderecoProperty());

        // Ação do botão Novo - limpa os campos
        btnNovo.setOnAction((e) -> {
            control.limparCampos();
        });

        btnSalvar.setOnAction(e -> {
            String mensagemErro = control.validar();
            if (!mensagemErro.isEmpty()) {
                new Alert(AlertType.WARNING, mensagemErro).show();
            } else {
                control.salvar();
                new Alert(AlertType.INFORMATION, "Paciente salvo com sucesso!").show();
            }
        });

        btnPesquisar.setOnAction((e) -> control.pesquisar());

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, selecionado) -> control.fromEntity(selecionado));

        // Criando as colunas da tabela
        TableColumn<Paciente, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getId().toString()));

        TableColumn<Paciente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getNome()));

        TableColumn<Paciente, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getCpf()));

        TableColumn<Paciente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getTelefone()));

        TableColumn<Paciente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getEmail()));

        TableColumn<Paciente, String> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getEndereco()));

        TableColumn<Paciente, String> colDataNascimento = new TableColumn<>("Data Nascimento");
        colDataNascimento.setCellValueFactory(item -> {
            LocalDate data = item.getValue().getDataNascimento();
            String dataFormatada = data != null ? data.format(dtf) : "";
            return new ReadOnlyStringWrapper(dataFormatada);
        });

        TableColumn<Paciente, String> colPeso = new TableColumn<>("Peso (kg)");
        colPeso.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getPeso().toString()));

        TableColumn<Paciente, String> colAltura = new TableColumn<>("Altura (m)");
        colAltura.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getAltura().toString()));

        TableColumn<Paciente, Void> colAcoes = new TableColumn<>("Ações");

        // O callback define como cada célula da coluna de ações é renderizada
        Callback<TableColumn<Paciente, Void>, TableCell<Paciente, Void>> callback = new Callback<>() {
            public TableCell<Paciente, Void> call(TableColumn<Paciente, Void> column) {
                return new TableCell<Paciente, Void>() {

                    Button btnExcluir = new Button("Excluir");

                    {
                        btnExcluir.setOnAction(e -> {
                            Alert alert = new Alert(
                                    AlertType.CONFIRMATION,
                                    "Deseja excluir este paciente?",
                                    ButtonType.YES, ButtonType.NO);
                            alert.setTitle("Confirmar exclusao");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.YES) {
                                control.excluir(getIndex());
                            }
                        });
                    }

                    // updateItem é chamado pelo JavaFX para renderizar cada célula
                    @Override
                    public void updateItem(Void param, boolean empty) {
                        super.updateItem(param, empty);
                        setGraphic(empty ? null : btnExcluir);
                    }
                };
            }
        };

        colAcoes.setCellFactory(callback);

        // Adicionando as colunas à tabela
        table.getColumns().add(colId);
        table.getColumns().add(colNome);
        table.getColumns().add(colCpf);
        table.getColumns().add(colTelefone);
        table.getColumns().add(colEmail);
        table.getColumns().add(colEndereco);
        table.getColumns().add(colDataNascimento);
        table.getColumns().add(colPeso);
        table.getColumns().add(colAltura);
        table.getColumns().add(colAcoes);

        table.setItems(control.getLista());
        return painelPrincipal;
    }
}
