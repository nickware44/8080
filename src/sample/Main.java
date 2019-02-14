package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("UI.fxml"));
        primaryStage.setTitle("8080");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.show();

        RegisterSystem RS = new RegisterSystem();
        CommandSystem CS = new CommandSystem(RS);

        System.out.println(CS.InputCommand("MVI A, FF"));
        //System.out.println(RS.getRegisterA());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
