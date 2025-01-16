package visao;

import com.petshop.petshop.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import database.DatabaseInit;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseInit.init();
            // Carrega a interface inicial
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login_view.fxml"));
            Parent root = fxmlLoader.load();

            primaryStage.setTitle("Pet Shop");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
