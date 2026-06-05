package edu.fatec.paciente.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import edu.fatec.paciente.controls.PacienteControl;
import edu.fatec.paciente.model.Paciente;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class PacienteBoundary extends Application {
    private PacienteControl control = new PacienteControl();
    private TextField id = new TextField();
    private TextField nome = new TextField();
    private TextField cpf = new TextField();
    private TextField telefone = new TextField();
    private TextField email = new TextField();
    private TextField endereco = new TextField();
    private TextField dataNascimento = new TextField();
    private TextField peso = new TextField();
    private TextField altura = new TextField();


    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Paciente pacienteCarregado = null;
    private Long proximoId = 1L;

    public Paciente toEntity() {
        Paciente p = new Paciente();
        p.setNome(nome.getText());
        p.setCpf(cpf.getText());
        p.setTelefone(telefone.getText());
        p.setEmail(email.getText());
        p.setEndereco(endereco.getText());
        LocalDate data = LocalDate.parse(dataNascimento.getText(), dtf);
        p.setDataNascimento(data);
        p.setPeso(Double.parseDouble(peso.getText()));
        p.setAltura(Double.parseDouble(altura.getText()));
        control.salvar(p);
        return p;
    }

    public void salvarOuAtualizar() {
        Paciente p = new Paciente();
        p.setNome(nome.getText());
        p.setCpf(cpf.getText());
        p.setTelefone(telefone.getText());
        p.setEmail(email.getText());
        p.setEndereco(endereco.getText());
        LocalDate data = LocalDate.parse(dataNascimento.getText(), dtf);
        p.setDataNascimento(data);
        p.setPeso(Double.parseDouble(peso.getText()));
        p.setAltura(Double.parseDouble(altura.getText()));
        
        if (pacienteCarregado != null) {
            // Edição de paciente existente
            Long idEditado = Long.parseLong(id.getText());
            p.setId(idEditado);
            control.atualizar(p);
            pacienteCarregado = p;
        } else {
            // Novo paciente - usar o ID exibido no campo
            Long idNovoRegistro = Long.parseLong(id.getText());
            p.setId(idNovoRegistro);
            
            // Incrementar proximoId para o próximo registro
            proximoId = idNovoRegistro + 1;
            
            control.salvar(p);
            pacienteCarregado = p;
        }
    }

    public void toBoundary(Paciente p) {
        if (p != null) {
            pacienteCarregado = p;

            id.setText(p.getId().toString());
            nome.setText(p.getNome());
            cpf.setText(p.getCpf());
            telefone.setText(p.getTelefone());
            email.setText(p.getEmail());
            endereco.setText(p.getEndereco());
            String strData = p.getDataNascimento().format(dtf);
            dataNascimento.setText(strData);
            peso.setText(p.getPeso().toString());
            altura.setText(p.getAltura().toString());
        }
    }

    public void limparCampos() {
        id.setText(proximoId.toString());
        nome.setText("");
        cpf.setText("");
        telefone.setText("");
        email.setText("");
        endereco.setText("");
        dataNascimento.setText("");
        peso.setText("");
        altura.setText("");
        pacienteCarregado = null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    
        VBox mainContainer = new VBox();
        mainContainer.setId("mainContainer");
        mainContainer.setAlignment(Pos.TOP_CENTER);


        Label title = new Label("CADASTRO DE PACIENTES");
        title.setId("formTitle");

      
        GridPane grid = new GridPane();
        grid.setId("formGrid");
        grid.setHgap(15);
        grid.setVgap(15);

        Label lblId = new Label("ID");
        Label lblNome = new Label("Nome");
        Label lblCpf = new Label("CPF");
        Label lblTelefone = new Label("Telefone");
        Label lblEmail = new Label("Email");
        Label lblEndereco = new Label("Endereço");
        Label lblDataNascimento = new Label("Data de Nascimento");
        Label lblPeso = new Label("Peso (kg)");
        Label lblAltura = new Label("Altura (m)");

        id.setPromptText("Preenchido automaticamente");
        nome.setPromptText("Digite o nome completo");
        cpf.setPromptText("000.000.000-00");
        telefone.setPromptText("(00) 00000-0000");
        email.setPromptText("email@exemplo.com");
        endereco.setPromptText("Rua, número, cidade");
        dataNascimento.setPromptText("dd/MM/yyyy");
        peso.setPromptText("0.00");
        altura.setPromptText("0.00");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setId("btnSalvar");
        btnSalvar.setOnAction((e) -> {
            try {
                boolean isEdicao = pacienteCarregado != null;
                salvarOuAtualizar();
                if (isEdicao) {
                    new Alert(AlertType.INFORMATION, "Paciente editado com Sucesso!").show();
                } else {
                    new Alert(AlertType.INFORMATION, "Paciente adicionado com sucesso!").show();
                }
                limparCampos();
            } catch (Exception ex) {
                new Alert(AlertType.ERROR, "Erro ao salvar paciente: " + ex.getMessage()).show();
            }
        });

        Button btnPesquisar = new Button("Pesquisar");
        btnPesquisar.setId("btnPesquisar");
        btnPesquisar.setOnAction((e) -> {
            try {
                Paciente p = null;
                
                // Tentar pesquisar por ID primeiro
                if (!id.getText().isEmpty()) {
                    try {
                        Long idPesquisa = Long.parseLong(id.getText());
                        p = control.pesquisarPorId(idPesquisa);
                        if (p != null) {
                            toBoundary(p);
                            new Alert(AlertType.INFORMATION, "Paciente encontrado com sucesso!").show();
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        new Alert(AlertType.WARNING, "ID deve ser um número válido").show();
                        return;
                    }
                }
                
                // Se não achou por ID, pesquisar por nome
                String nomePesquisa = nome.getText();
                if (nomePesquisa.isEmpty()) {
                    new Alert(AlertType.WARNING, "Digite o ID ou o nome do paciente para pesquisar").show();
                    return;
                }
                
                p = control.pesquisar(nomePesquisa);
                if (p != null) {
                    toBoundary(p);
                    new Alert(AlertType.INFORMATION, "Paciente encontrado com sucesso!").show();
                } else {
                    new Alert(AlertType.WARNING, "Nenhum paciente encontrado").show();
                }
            } catch (Exception ex) {
                new Alert(AlertType.ERROR, "Erro ao pesquisar: " + ex.getMessage()).show();
            }
        });

        Button btnExcluir = new Button("Excluir");
        btnExcluir.setId("btnExcluir");
        btnExcluir.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-weight: bold;");
        btnExcluir.setOnAction((e) -> {
            try {
                if (pacienteCarregado != null) {
                    control.deletar(pacienteCarregado);
                    new Alert(AlertType.INFORMATION, "Paciente excluído com Sucesso!").show();
                    limparCampos();
                } else {
                    new Alert(AlertType.WARNING, "Nenhum paciente carregado para deletar").show();
                }
            } catch (Exception ex) {
                new Alert(AlertType.ERROR, "Erro ao excluir paciente: " + ex.getMessage()).show();
            }
        });

        // Preencher ID automaticamente com o próximo ID disponível do banco de dados
        proximoId = control.obterProximoId();
        id.setText(proximoId.toString());
        grid.add(lblId, 0, 0);
        grid.add(id, 1, 0);
        grid.add(lblNome, 0, 1);
        grid.add(nome, 1, 1);
        grid.add(lblCpf, 0, 2);
        grid.add(cpf, 1, 2);
        grid.add(lblTelefone, 0, 3);
        grid.add(telefone, 1, 3);
        grid.add(lblEmail, 0, 4);
        grid.add(email, 1, 4);
        grid.add(lblEndereco, 0, 5);
        grid.add(endereco, 1, 5);
        grid.add(lblDataNascimento, 0, 6);
        grid.add(dataNascimento, 1, 6);
        grid.add(lblPeso, 0, 7);
        grid.add(peso, 1, 7);
        grid.add(lblAltura, 0, 8);
        grid.add(altura, 1, 8);

     
        HBox buttonBox = new HBox();
        buttonBox.setId("buttonBox");
        buttonBox.setPrefHeight(50);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnSalvar, btnPesquisar, btnExcluir);
        grid.add(buttonBox, 0, 9);
        GridPane.setColumnSpan(buttonBox, 2);

        // Adicionando elementos ao container principal
        mainContainer.getChildren().addAll(title, grid);

        // Cena e estilo
        Scene scn = new Scene(mainContainer, 600, 650);
        String resource = getClass().getResource("/styles.css").toExternalForm();
        scn.getStylesheets().add(resource);

        primaryStage.setScene(scn);
        primaryStage.setTitle("Sistema de Gestão de Pacientes");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

}
