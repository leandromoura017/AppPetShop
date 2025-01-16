package controller;

import persistencia.AgendamentoRepository;
import persistencia.PetRepository;
import persistencia.ServicoRepository;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class MainController {

    @FXML
    private Button botaoGerenciamento;

    private PetRepository petRepo;
    private ServicoRepository servicoRepo;
    private AgendamentoRepository agendamentoRepo;

    public MainController() {
        this.petRepo = new PetRepository();
        this.servicoRepo = new ServicoRepository();
        this.agendamentoRepo = new AgendamentoRepository();
    }

    @FXML
    private void initialize() {
        botaoGerenciamento.setOnAction(event -> abrirGerenciamento());
    }

    private void abrirGerenciamento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petshop/petshop/gerenciamento_view.fxml"));
            Parent root = loader.load();

            GerenciamentoController gerenciamentoController = loader.getController();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gerenciamento de Pets");
            stage.show();

            Stage currentStage = (Stage) botaoGerenciamento.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
