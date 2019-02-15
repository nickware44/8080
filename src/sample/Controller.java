package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    private ObservableList<RegisterTableLine> RegisterTableList = FXCollections.observableArrayList();

    @FXML
    Pane UICodePanel;

    @FXML
    TextArea UICodeArea;

    @FXML
    TextArea UILogArea;

    @FXML
    TableView UIRegisterTable;

    @FXML
    TableColumn<RegisterTableLine, String> UIRegisterTableNameColumn;

    @FXML
    TableColumn<RegisterTableLine, String> UIRegisterTableValueColumn;

    private Stage mStage;
    private int StepNum;
    private CommandSystem CS;
    private RegisterSystem RS;
    private String ActiveDataFileName;

    public void initialize() {
        StepNum = 0;
        ActiveDataFileName = "";
        ShowLineNums();
        UIRegisterTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        UIRegisterTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    @FXML
    public void onCodeTextChange() {
        UICodeArea.selectPositionCaret(UICodeArea.getLength());
        UICodeArea.deselect();
        ShowLineNums();

    }

    @FXML
    public void onCodeTextScroll() {
        System.out.println("~~");
        ShowLineNums();
    }

    @FXML
    public void FileNew() {
        ActiveDataFileName = "";
        UICodeArea.clear();
        ShowLineNums();
    }

    @FXML
    public void FileOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("8080 emulator files (*.8080", "*.8080");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mStage);
        if (file != null) {
            int i;
            ActiveDataFileName = file.getAbsolutePath();
            try {
                FileReader FR = new FileReader(file);
                while ((i = FR.read()) != -1) {
                    Character C = (char)i;
                    UICodeArea.appendText(C.toString());
                }
                ShowLineNums();
                Send2Log("[Open file] File opened: "+ActiveDataFileName);
            } catch (java.io.IOException ex) {
                Send2Log("[Open file] Error opening file "+ActiveDataFileName);
                return;
            }
        }

    }

    @FXML
    public void FileSave() {
        if (ActiveDataFileName.equals("")) {
            FileSaveAs();
            return;
        }

        try {
            FileWriter FW = new FileWriter(ActiveDataFileName);
            FW.write(UICodeArea.getText());
            FW.close();
            Send2Log("[Save to file] Saved to file: "+ActiveDataFileName);

        } catch (java.io.IOException ex) {
            Send2Log("[Save to file] Error saving to file "+ActiveDataFileName);
        }
    }

    @FXML
    public void FileSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file as...");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("8080 emulator files (*.8080", "*.8080");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mStage);

        if (file != null) {
            ActiveDataFileName = file.getAbsolutePath() + ".8080";
            FileSave();
        }
    }

    @FXML
    public void Close() {
        Platform.exit();
    }

    @FXML
    public void RunCodeFull() {
        ResetSystem();
        boolean ErrFlag = false;
        String CurrentLine;
        for (int i = 0; i < getCodeAreaLineCount(); i++) {
            CurrentLine = UICodeArea.getText().split("\n").clone()[i];
            if (!CurrentLine.equals("") && CS.InputCommand(CurrentLine) == -1) {
                Send2Log("[Run] Error on line "+(i+1));
                ErrFlag = true;
                break;
            }
        }

        if (!ErrFlag) {
            Send2Log("[Run] Success");
            UpdateRegisterTable();
        }
    }

    @FXML
    public void RunCodeStep() {
        if (StepNum >= getCodeAreaLineCount()) StepNum = 0;
        String CurrentLine = UICodeArea.getText().split("\n").clone()[StepNum];
        if (!CurrentLine.equals("") && CS.InputCommand(CurrentLine) == -1) {
            Send2Log("[Run step] Error on line "+(StepNum+1));
        } else {
            StepNum++;
            UpdateRegisterTable();
        }
    }

    @FXML
    public void ResetSystem() {
        CS.InputCommand("HLT");
    }

    @FXML
    public void ClearLog() {
        UILogArea.clear();
    }

    private void Send2Log(String Msg) {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        UILogArea.appendText(formatForDateNow.format(date)+" ~ "+Msg+"\n");
    }

    private int getCodeAreaLineCount() {
        return UICodeArea.getText().split("\n").length;
    }

    private void ShowLineNums() {
        UICodePanel.getChildren().clear();
        if (UICodeArea.getScrollTop() != 0) {
            //UICodeArea.appendText("");
            //UICodeArea.deletePreviousChar();
        }
        //System.out.println(UICodeArea.getScrollTop());
        for (Integer i = 1; i < getCodeAreaLineCount()+1; i++) {
            Label L = new Label();
            L.setText(i.toString());
            L.setLayoutX(5);
            L.setLayoutY(((i-1)*16+5)-UICodeArea.getScrollTop());
            L.setStyle("-fx-font-size: 11px; -fx-text-fill: #FFF;");
            UICodePanel.getChildren().add(L);
        }

    }

    private void UpdateRegisterTable() {
        RegisterTableList.clear();
        RegisterTableList.add(new RegisterTableLine("A", RS.getRegisterA()));
        RegisterTableList.add(new RegisterTableLine("B", RS.getRegisterB()));
        RegisterTableList.add(new RegisterTableLine("C", RS.getRegisterC()));
        RegisterTableList.add(new RegisterTableLine("D", RS.getRegisterD()));
        RegisterTableList.add(new RegisterTableLine("E", RS.getRegisterE()));
        RegisterTableList.add(new RegisterTableLine("H", RS.getRegisterH()));
        RegisterTableList.add(new RegisterTableLine("L", RS.getRegisterL()));
        //RegisterTableList.add(new RegisterTableLine("M", RS.getRegisterM()));
        UIRegisterTable.setItems(RegisterTableList);
    }

    public void setStage(Stage _mStage) {
        mStage = _mStage;
    }

    public void setCS(CommandSystem _CS) {
        CS = _CS;
    }

    public void setRS(RegisterSystem _RS) {
        RS = _RS;
        UpdateRegisterTable();
    }
}
