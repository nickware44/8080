package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class Controller {
    @FXML
    TextArea UICodeArea;

    @FXML
    TextArea UILogArea;

    private int StepNum;
    private CommandSystem CS;

    public Controller() {
        StepNum = 0;
    }

    @FXML
    public void RunCodeFull() {
        System.out.println(getCodeAreaLineCount());
        for (int i = 0; i < getCodeAreaLineCount(); i++) {
            if (CS.InputCommand(UICodeArea.getText().split("\n").clone()[i]) == -1) {
                Send2Log("[Run] Error on line "+(i+1));
            }
        }
    }

    @FXML
    public void RunCodeStep() {
        if (StepNum >= getCodeAreaLineCount()) StepNum = 0;
        if (CS.InputCommand(UICodeArea.getText().split("\n").clone()[StepNum]) == -1) {
            Send2Log("[Run step] Error on line "+(StepNum+1));
            StepNum = 0;
        } else StepNum++;
    }

    private void Send2Log(String Msg) {
        UILogArea.appendText(Msg+"\n");
    }

    private int getCodeAreaLineCount() {
        return UICodeArea.getText().split("\n").length;
    }

    public void setCS(CommandSystem _CS) {
        CS = _CS;
    }
}
