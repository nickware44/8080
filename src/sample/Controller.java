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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {
    private ObservableList<RegisterTableLine> RegisterTableList = FXCollections.observableArrayList();
    private ObservableList<FlagTableLine> FlagTableList = FXCollections.observableArrayList();
    private ObservableList<PortTableLine> PortTableList = FXCollections.observableArrayList();
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

    @FXML
    TableView UIFlagTable;

    @FXML
    TableColumn<FlagTableLine, String> UIFlagTableNameColumn;

    @FXML
    TableColumn<FlagTableLine, String> UIFlagTableStateColumn;

    @FXML
    TableView UIPortTable;

    @FXML
    TableColumn<PortTableLine, String> UIPortTableNumColumn;

    @FXML
    TableColumn<PortTableLine, String> UIPortTableValueColumn;

    private Stage mStage;
    private int StepNum;
    private CommandSystem CS;
    private RegisterSystem RS;
    private MemorySystem MS;
    private PortSystem PS;
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
        UIFlagTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("flagName"));
        UIFlagTableStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        UIPortTableNumColumn.setCellValueFactory(new PropertyValueFactory<>("portNum"));
        UIPortTableValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

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
    public void DumpMemory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Dump memory to file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(mStage);
        try {
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(CS.getCommandCounter());
            for (int i = 0; i < MS.getMemory().length; i++) {
                out.write(MS.getMemory()[i]);
            }
            out.flush();
            out.close();
            Send2Log("[Memory] Dump success");
        } catch (java.io.IOException ex) {
            Send2Log("[Memory] Unable to dump memory to file "+ActiveDataFileName);
        }
    }

    @FXML
    public void LoadMemory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load memory dump from file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mStage);
        try {
            FileReader fstream = new FileReader(file);
            BufferedReader in = new BufferedReader(fstream);
            CS.setCommandCounter(in.read());
            for (int i = 0; i < MS.getMemory().length; i++) {
                MS.getMemory()[i] = (short)in.read();
            }
            in.close();
            Send2Log("[Memory] Load success");
            UpdateMemoryTable();
        } catch (java.io.IOException ex) {
            Send2Log("[Memory] Unable to load memory dump from file "+ActiveDataFileName);
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
            UpdateFlagTable();
            UpdatePortTable();
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
        UpdateFlagTable();
        UpdatePortTable();
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
        UpdateFlagTable();
        UpdatePortTable();
        UpdateMemoryTable();
    }

    @FXML
    public void RunFMemory() {
        CS.ExecuteCommands();
        if (ErrorAddress != -1 && ErrorFlag) Send2Log("[Run from memory] Error running command from memory address 0x"+MemoryTableLine.toHEX(ErrorAddress, 4));
        else {
            Send2Log("[Run from memory] Success");
            UpdateRegisterTable();
            UpdateFlagTable();
            UpdatePortTable();
            UpdateMemoryTable();
        }
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

        UIRegisterTable.setItems(RegisterTableList);
    }

    private void UpdateFlagTable() {
        FlagTableList.clear();
        FlagTableList.add(new FlagTableLine("CY", RS.getCYFlag()));
        FlagTableList.add(new FlagTableLine("P", RS.getPFlag()));
        FlagTableList.add(new FlagTableLine("AC", RS.getACFlag()));
        FlagTableList.add(new FlagTableLine("Z", RS.getZFlag()));
        FlagTableList.add(new FlagTableLine("S", RS.getSFlag()));

        UIFlagTable.setItems(FlagTableList);
    }

    private void UpdateMemoryTable() {
        MemoryTableList.clear();
        for (int i = 0; i < 2048; i++) {
            MemoryTableList.add(new MemoryTableLine(i, MS.getMemoryValue(i)));
        }
        UIMemoryTable.setItems(MemoryTableList);
    }

    private void UpdatePortTable() {
        PortTableList.clear();
        for (short i = 0; i < 3; i++) {
            PortTableList.add(new PortTableLine(i, PS.ReadPort(i)));
        }
        UIPortTable.setItems(PortTableList);
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
        UpdateFlagTable();
    }

    public void setMS(MemorySystem _MS) {
        MS = _MS;
        UpdateMemoryTable();
    }

    public void setPS(PortSystem _PS) {
        PS = _PS;
        UpdatePortTable();
    }
}
