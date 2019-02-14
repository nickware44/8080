package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    TextArea UICodeArea;

    private int StepNum;
    private CommandSystem CS;

    Controller() {
        StepNum = 0;
    }

    @FXML
    void RunCodeFull() {

    }

    @FXML
    void RunCodeStep() {

    }

    public void setCS(CommandSystem _CS) {
        CS = _CS;
    }
}
