package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UI.fxml"));
        Parent root = loader.load();
        final Controller C = loader.getController();
        primaryStage.setTitle("8080: microprocessor emulator");
        primaryStage.setScene(new Scene(root, 1150, 750));
        primaryStage.show();

        RegisterSystem RS = new RegisterSystem();
        MemorySystem MS = new MemorySystem();
        PortSystem PS = new PortSystem();
        CommandSystem CS = new CommandSystem(RS, MS, PS);

        C.setStage(primaryStage);
        C.setCS(CS);
        C.setRS(RS);
        C.setMS(MS);
        C.setPS(PS);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
