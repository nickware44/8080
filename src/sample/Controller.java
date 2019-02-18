package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    private ObservableList<RegisterTableLine> RegisterTableList = FXCollections.observableArrayList();
    private ObservableList<MemoryTableLine> MemoryTableList = FXCollections.observableArrayList();

    @FXML
    ScrollPane UICodePanel;

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

    @FXML
    TableView UIMemoryTable;

    @FXML
    TableColumn<MemoryTableLine, String> UIMemoryTableAddressColumn;

    @FXML
    TableColumn<MemoryTableLine, String> UIMemoryTableValueColumn;

    private Stage mStage;
    private int StepNum;
    private CommandSystem CS;
    private RegisterSystem RS;
    private MemorySystem MS;
    private String ActiveDataFileName;
    private boolean RunSuccessFlag;
    private boolean ErrorFlag;
    private int ErrorLine;
    private int ErrorAddress;

    public void initialize() {
        StepNum = 0;
        ActiveDataFileName = "";
        ShowLineNums();
        UIRegisterTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        UIRegisterTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        UIMemoryTableAddressColumn.setCellValueFactory(new PropertyValueFactory<>("memoryAddress"));
        UIMemoryTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        UICodeArea.scrollTopProperty().addListener((obs, oldVal, newVal) -> ShiftLineNums());
        UICodePanel.vvalueProperty().addListener((obs, oldVal, newVal) -> ShiftText());
    }

    @FXML
    public void onCodeTextChange() {
        ErrorLine = -1;
        ErrorFlag = false;
        RunSuccessFlag = false;
        StepNum = 0;
        ShowLineNums();
    }

    @FXML
    public void FileNew() {
        ActiveDataFileName = "";
        UICodeArea.clear();
        ErrorLine = -1;
        ErrorFlag = false;
        RunSuccessFlag = false;
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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("8080 emulator files (*.8080)", "*.8080");
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
        Load2Memory();
        if (ErrorLine == -1) {
            CS.ExecuteCommands();
            Send2Log("[Run] Success");
            RunSuccessFlag = true;
            UpdateRegisterTable();
            UpdateMemoryTable();
        }
        ShowLineNums();
    }

    @FXML
    public void RunCodeStep() {
        if (StepNum >= getCodeAreaLineCount()) {
            RunSuccessFlag = true;
            Send2Log("[Run step] Success");
            StepNum = 0;
            ShowLineNums();
            return;
        }
        if (StepNum == 0) {
            ResetSystem();
            Load2Memory();
            if (ErrorLine != -1) {
                ShowLineNums();
                return;
            }
        }

        CS.ExecuteNextCommand();
        StepNum++;
        UpdateRegisterTable();
        UpdateMemoryTable();

        ShowLineNums();
    }

    @FXML
    public void ResetSystem() {
        CS.InputCommand("HLT");
        CS.ExecuteNextCommand();
        StepNum = 0;
        ErrorLine = -1;
        ErrorFlag = false;
        RunSuccessFlag = false;
        Send2Log("[Reset] Done");
        ShowLineNums();
        UpdateRegisterTable();
        UpdateMemoryTable();
    }

    @FXML
    public void ClearLog() {
        UILogArea.clear();
    }

    private void Load2Memory() {
        String CurrentLine;
        for (int i = 0; i < getCodeAreaLineCount(); i++) {
            CurrentLine = UICodeArea.getText().split("\n").clone()[i];
            if (!CurrentLine.equals("") && (ErrorAddress = CS.InputCommand(CurrentLine)) != -1) {
                ErrorFlag = false;
                ErrorLine = i + 1;
                Send2Log("[Loader] Error loading command from line "+(ErrorLine)+" to memory address 0x"+MemoryTableLine.toHEX(ErrorAddress, 4));
                UpdateMemoryTable();
                return;
            }
        }
        Send2Log("[Loader] Success");
        MS.ResetIterator();
    }

    private void Send2Log(String Msg) {
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        UILogArea.appendText(formatForDateNow.format(date)+" ~ "+Msg+"\n");
    }

    private int getCodeAreaLineCount() {
        return StringUtils.countMatches(UICodeArea.getText(), "\n") + 1;
    }

    private void ShowLineNums() {
        VBox root = new VBox();
        for (Integer i = 1; i < getCodeAreaLineCount()+1; i++) {
            Label L = new Label();
            L.setText(i.toString());
            if (i == 1) L.paddingProperty().setValue(new Insets(4.5,0,0,10));
            else if (i == getCodeAreaLineCount()) L.paddingProperty().setValue(new Insets(0.2,0,3,10));
            else L.paddingProperty().setValue(new Insets(0.2,0,0,10));

            if (i < StepNum || RunSuccessFlag || (i < ErrorLine && ErrorFlag)) L.setStyle("-fx-font-size: 11px; -fx-text-fill: #4BC44D;");
            else if (i == StepNum) L.setStyle("-fx-font-size: 11px; -fx-text-fill: #009BFF;");
            else L.setStyle("-fx-font-size: 11px; -fx-text-fill: #FFF;");

            if (i == ErrorLine) L.setStyle("-fx-font-size: 11px; -fx-text-fill: #F72C2C;");


            root.getChildren().add(L);
            UICodePanel.setContent(root);
        }
        ShiftLineNums();
    }

    private void ShiftLineNums() {
        if (getCodeAreaLineCount() > 1) UICodePanel.setVvalue(((ScrollPane)UICodeArea.getChildrenUnmodifiable().get(0)).getVvalue());

    }

    private void ShiftText() {
        if (getCodeAreaLineCount() > 1) ((ScrollPane)UICodeArea.getChildrenUnmodifiable().get(0)).setVvalue(UICodePanel.getVvalue());
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

    private void UpdateMemoryTable() {
        MemoryTableList.clear();
        for (int i = 0; i < 2048; i++) {
            MemoryTableList.add(new MemoryTableLine(i, MS.getMemoryValue(i)));
        }
        UIMemoryTable.setItems(MemoryTableList);
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

    public void setMS(MemorySystem _MS) {
        MS = _MS;
        UpdateMemoryTable();
    }
}
