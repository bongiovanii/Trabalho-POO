package edu.fatec.medico.view;

import java.util.Optional;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import edu.fatec.medico.controller.MedicoController;
import edu.fatec.medico.model.Medico;

public class MedicoBoundary{
    private MedicoController controller = new MedicoController();

    private TextField nome = new TextField();
    private TextField crm = new TextField();
    private TextField cpf = new TextField();
    private TextField especialidade = new TextField();
    private TextField telefone = new TextField();

    private TableView<Medico> table = new TableView<>();

    public Pane render() {
        BorderPane painelPrincipal = new BorderPane();
        GridPane painelCampos = new GridPane(); 
        painelCampos.setHgap(10); 
        painelCampos.setVgap(8); 

        painelCampos.add(new Label("Nome: "), 0,1);
        painelCampos.add(nome, 1,1);

        painelCampos.add(new Label("CRM: "), 0,2);
        painelCampos.add(crm, 1,2);

        painelCampos.add(new Label("CPF: "), 0,3);
        painelCampos.add(cpf, 1,3);

        painelCampos.add(new Label("Especialidade: "), 0,4);
        painelCampos.add(especialidade, 1,4);

        painelCampos.add(new Label("Telefone: "), 0,5);
        painelCampos.add(telefone, 1,5);

        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnPesquisar = new Button("Pesquisar");
        Button btnNovo = new Button("Novo");

        painelCampos.add(btnNovo,0,6);
        painelCampos.add(btnSalvar, 1, 6);
        painelCampos.add(btnPesquisar, 2 , 6);
        painelCampos.add(btnExcluir, 3, 6);

        painelPrincipal.setTop(painelCampos);
        painelPrincipal.setCenter(table);

        javafx.beans.binding.Bindings.bindBidirectional(nome.textProperty(), controller.nomeProperty());
        javafx.beans.binding.Bindings.bindBidirectional(crm.textProperty(), controller.crmProperty(), new NumberStringConverter());
        javafx.beans.binding.Bindings.bindBidirectional(cpf.textProperty(), controller.cpfProperty(), new NumberStringConverter());
        javafx.beans.binding.Bindings.bindBidirectional(especialidade.textProperty(), controller.especialidadeProperty());
        javafx.beans.binding.Bindings.bindBidirectional(telefone.textProperty(), controller.telefoneProperty());

        btnNovo.setOnAction(e ->{
            controller.limparCampos();
        });

        btnSalvar.setOnAction(e -> {
            String error = controller.validar();
            if(!error.isEmpty()){
                new Alert(AlertType.WARNING, error).show();
            } else{
                controller.salvar();
                new Alert(AlertType.INFORMATION, "Médico salvo com sucesso!").show();
            }
        });

        btnPesquisar.setOnAction(e -> {
            controller.pesquisar();
        });

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, anterior, selecionado) -> controller.fromEntity(selecionado)
        );

        TableColumn<Medico, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getId().toString()));

        TableColumn<Medico, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getNome()));

        TableColumn<Medico, String> colCrm = new TableColumn<>("CRM");
        colCrm.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getCrm().toString()));

        TableColumn<Medico, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getCpf().toString()));

        TableColumn<Medico, String> colEspecilidade = new TableColumn<>("Especialidade");
        colEspecilidade.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getEspecialidade()));

        TableColumn<Medico, String> colTel = new TableColumn<>("Telefone");
        colTel.setCellValueFactory(item -> new ReadOnlyStringWrapper(item.getValue().getTelefone()));

        TableColumn<Medico, Void> colAcoes = new TableColumn<>("Ações");

        Callback<TableColumn<Medico, Void>, TableCell<Medico, Void>> callback = new Callback<>() {
            @Override
            public TableCell<Medico, Void> call(TableColumn<Medico, Void> column) {
                return new TableCell<>() {
                    private final Button btnExcluirCol = new Button("Excluir");

                    {
                        btnExcluirCol.setOnAction(e -> {
                            Medico medico = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(
                                    AlertType.CONFIRMATION,
                                    "Deseja excluir este médico?",
                                    ButtonType.YES,
                                    ButtonType.NO);
                            alert.setTitle("Confirmar exclusão");

                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.YES) {
                                controller.excluir(getIndex());
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnExcluirCol);
                    }
                };
            }
        };

        colAcoes.setCellFactory(callback);

        table.getColumns().addAll(colId, colNome, colCrm, colCpf, colEspecilidade, colTel, colAcoes);
        table.setItems(controller.getLista());

        return painelPrincipal;
    }
}
