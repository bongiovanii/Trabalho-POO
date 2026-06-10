package edu.fatec;

import edu.fatec.consulta.view.ConsultaBoundary;
import edu.fatec.exame.view.ExameBoundary;
import edu.fatec.medico.view.MedicoBoundary;
import edu.fatec.paciente.view.PacienteBoundary;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PrincipalBoundary extends Application {
    private BorderPane pane = new BorderPane();
    private Pane pacientePane;
    private Pane medicoPane;
    private Pane examePane;
    private Pane consultaPane;
    private Pane homePane;
   

     @Override
    public void start(Stage stage) { 
        Scene scn = new Scene(pane, 800, 600);
        
        carregarHomePane();
        pane.setCenter(homePane);

        MenuBar menuBar = new MenuBar();

        Menu mnuArquivo = new Menu("Sistema");
        MenuItem mnuHomeItem = new MenuItem("Início");
        mnuArquivo.getItems().add(mnuHomeItem);
        
        Menu mnuCadastro = new Menu("Cadastro");

        MenuItem mnuPacienteItem = new MenuItem("Pacientes");
        MenuItem mnuMedicoItem = new MenuItem("Médicos");
        MenuItem mnuExameItem = new MenuItem("Exames");
        MenuItem mnuConsultaItem = new MenuItem("Consultas");

        menuBar.getMenus().addAll( mnuArquivo, mnuCadastro );

        mnuCadastro.getItems().addAll( mnuPacienteItem, mnuMedicoItem, mnuExameItem, mnuConsultaItem );

        pane.setTop( menuBar );

        mnuHomeItem.setOnAction( e -> {
            carregarHomePane();
            pane.setCenter( homePane );
        });

        mnuPacienteItem.setOnAction( e -> {
            carregarPacientePane();
            pane.setCenter( pacientePane );
        });

        
        mnuMedicoItem.setOnAction( e -> {
            carregarMedicoPane();
            pane.setCenter( medicoPane );
        });
        
        mnuExameItem.setOnAction( e -> {
            carregarExamePane();
            pane.setCenter( examePane );
        });

        mnuConsultaItem.setOnAction( e -> {
            carregarConsultaPane();
            pane.setCenter( consultaPane );
        });


        stage.setScene(scn);
        stage.setTitle("Gestão de Clínica Médica");
        stage.show();
    }

    /**
     * Monta uma tela inicial com botões de atalho para as principais funções do sistema.
     */
    private void carregarHomePane() {
        if (homePane == null) {
            VBox vbox = new VBox(30);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-background-color: #f4f4f4;");

            Label lblTitulo = new Label("Bem-vindo ao Sistema de Gestão Clínica");
            lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 32));
            lblTitulo.setStyle("-fx-text-fill: #2c3e50;");

            Label lblSubtitulo = new Label("Selecione uma opção para começar:");
            lblSubtitulo.setFont(Font.font("Arial", 18));

            HBox botoesAtalho = new HBox(20);
            botoesAtalho.setAlignment(Pos.CENTER);

            Button btnPacientes = criarBotaoAtalho("Pacientes");
            Button btnMedicos = criarBotaoAtalho("Médicos");
            Button btnConsultas = criarBotaoAtalho("Consultas");
            Button btnExames = criarBotaoAtalho("Exames");

            btnPacientes.setOnAction(e -> { carregarPacientePane(); pane.setCenter(pacientePane); });
            btnMedicos.setOnAction(e -> { carregarMedicoPane(); pane.setCenter(medicoPane); });
            btnConsultas.setOnAction(e -> { carregarConsultaPane(); pane.setCenter(consultaPane); });
            btnExames.setOnAction(e -> { carregarExamePane(); pane.setCenter(examePane); });

            botoesAtalho.getChildren().addAll(btnPacientes, btnMedicos, btnConsultas, btnExames);

            vbox.getChildren().addAll(lblTitulo, lblSubtitulo, botoesAtalho);
            homePane = vbox;
        }
    }

    /**
     * Auxiliar para padronizar o visual dos botões do dashboard
     */
    private Button criarBotaoAtalho(String texto) {
        Button btn = new Button(texto);
        btn.setPrefSize(150, 100);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setStyle("-fx-text-alignment: center; -fx-cursor: hand;");
        return btn;
    }
    
    private void carregarPacientePane() {
        if (pacientePane == null) {
            try {
                pacientePane = new PacienteBoundary().render();
                System.out.println("Painel de Pacientes carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Pacientes:");
                e.printStackTrace();
            }
        }
    }


    private void carregarMedicoPane() {
        if (medicoPane == null) {
            try {
                medicoPane = new MedicoBoundary().render();
                System.out.println("Painel de Médicos carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Médicos:");
                e.printStackTrace();
            }
        }
    }
    
    
    private void carregarExamePane() {
        if (examePane == null) {
            try {
                examePane = new ExameBoundary().render();
                System.out.println("Painel de Exames carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Exames:");
                e.printStackTrace();
            }
        }
    }


    private void carregarConsultaPane() {
        if (consultaPane == null) {
            try {
                consultaPane = new ConsultaBoundary().render();
                System.out.println("Painel de Consultas carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Consultas:");
                e.printStackTrace();
            }
        }
    }
    
}
