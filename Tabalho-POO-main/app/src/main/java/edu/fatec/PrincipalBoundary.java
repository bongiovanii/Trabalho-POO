package edu.fatec;

import edu.fatec.exame.view.ExameBoundary;
import edu.fatec.paciente.view.PacienteBoundary;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrincipalBoundary extends Application {
    private BorderPane pane = new BorderPane();
    private Pane pacientePane;
    private Pane examePane;
   

     @Override
    public void start(Stage stage) { 
        Scene scn = new Scene(pane, 800, 600);
        
        // Inicia com o painel de pacientes (lazy-loaded quando necessário)
        carregarPacientePane();
        pane.setCenter(pacientePane);

        MenuBar menuBar = new MenuBar();

        Menu mnuArquivo = new Menu("Arquivo");
        Menu mnuCadastro = new Menu("Cadastro");
        Menu mnuAjuda = new Menu("Ajuda");

        MenuItem mnuPacienteItem = new MenuItem("Pacientes");
        MenuItem mnuMedicoItem = new MenuItem("Medicos");
        MenuItem mnuExameItem = new MenuItem("Exames");
        MenuItem mnuConsultaItem = new MenuItem("Consultas");

        menuBar.getMenus().addAll( mnuArquivo, mnuCadastro, mnuAjuda);

        mnuCadastro.getItems().addAll( mnuPacienteItem, mnuMedicoItem, mnuExameItem, mnuConsultaItem );

        pane.setTop( menuBar );


        mnuPacienteItem.setOnAction( e -> {
            carregarPacientePane();
            pane.setCenter( pacientePane );
        });
        
        mnuExameItem.setOnAction( e -> {
            carregarExamePane();
            pane.setCenter( examePane );
        });

        stage.setScene(scn);
        stage.setTitle("Gestao de Clinica Medica");
        stage.show();
    }
    
    /**
     * Carrega o painel de Pacientes de forma segura
     */
    private void carregarPacientePane() {
        if (pacientePane == null) {
            try {
                pacientePane = new PacienteBoundary().render();
                System.out.println("Painel de Pacientes carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Pacientes:");
                e.printStackTrace();
                // Cria um painel vazio em caso de erro
                pacientePane = new javafx.scene.layout.VBox();
                javafx.scene.control.Label lblErro = new javafx.scene.control.Label(
                    "Erro ao carregar painel de Pacientes. Verifique a conexão com o banco de dados.");
                ((javafx.scene.layout.VBox) pacientePane).getChildren().add(lblErro);
            }
        }
    }
    
    /**
     * Carrega o painel de Exames de forma segura
     */
    private void carregarExamePane() {
        if (examePane == null) {
            try {
                examePane = new ExameBoundary().render();
                System.out.println("Painel de Exames carregado com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro ao carregar painel de Exames:");
                e.printStackTrace();
                // Cria um painel vazio em caso de erro
                examePane = new javafx.scene.layout.VBox();
                javafx.scene.control.Label lblErro = new javafx.scene.control.Label(
                    "Erro ao carregar painel de Exames. Verifique a conexão com o banco de dados.");
                ((javafx.scene.layout.VBox) examePane).getChildren().add(lblErro);
            }
        }
    }
    
}

