package csi.tp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("helloView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Jeu Serpents et Échelles");
        stage.setScene(scene);

        // Dimensions minimales
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Dimensions maximales
        stage.setMaxWidth(1200);
        stage.setMaxHeight(700);

        stage.show();
    }
}