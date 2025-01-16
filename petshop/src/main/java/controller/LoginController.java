package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import dominio.Usuario;

public class LoginController {

    @FXML
    private TextField usuario;

    @FXML
    private PasswordField senha;

    @FXML
    private Button btnEntrar;

    private Usuario usuarioCorreto = new Usuario("leandro", "1234");

    @FXML
    public void initialize() {
        btnEntrar.setOnAction(event -> fazerLogin());
    }
    @FXML
    private void fazerLogin() {
        String usuarioInput = usuario.getText();
        String senhaInput = senha.getText();

        // Verificação de usuário e senha
        if (usuarioInput.equals(usuarioCorreto.getUsuario()) && senhaInput.equals(usuarioCorreto.getSenha())) {
            abrirTelaGerenciamento();
        } else {
            mostrarAlerta("Usuário ou senha incorretos.");
        }
    }

    private void abrirTelaGerenciamento() {
        try {
            // Carregar a nova tela de gerenciamento
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/petshop/petshop/gerenciamento_view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnEntrar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Tela de Gerenciamento");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao abrir a tela de gerenciamento.");
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}