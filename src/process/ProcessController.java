package process;

import file.domain.ExecutableFile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import process.action.ProcessAction;
import process.action.SchedulingAction;
import process.domain.MyProcess;
import process.thread.CPUThread;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author longting
 **/
public class ProcessController implements Initializable {

    public TableView readyTable;
    public TableColumn readyIdColumn;
    public TableColumn reachTimeColumn;
    public TableView blockTable;
    public TableColumn blockIdColumn;
    public TableColumn blockReasonColumn;
    public TextField runningId;
    public TextField currentInstruction;
    public TextField intermediateResult;
    public TextField executionTime;
    public ComboBox comboBox;
    public Button startButton;

    public ProcessController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<MyProcess> readyProcess = ProcessAction.readyProcessQueue.getQueue();
        this.readyTable.setItems(FXCollections.observableList(readyProcess));
        this.readyIdColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.reachTimeColumn.setCellValueFactory(new PropertyValueFactory("reachTime"));

        List<MyProcess> blockProcess = ProcessAction.blockProcessQueue.getQueue();
        this.blockTable.setItems(FXCollections.observableList(blockProcess));
        this.blockIdColumn.setCellValueFactory(new PropertyValueFactory("name"));
        this.blockReasonColumn.setCellValueFactory(new PropertyValueFactory("reason"));

        this.comboBox.setItems(FXCollections.observableList(SchedulingAction.executableFiles));

    }

    private void setMonitor() {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                ((Stage) startButton.getScene().getWindow()).setAlwaysOnTop(false);
                Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,"进程管理关闭", new ButtonType("确认", ButtonBar.ButtonData.YES), new ButtonType("取消", ButtonBar.ButtonData.NO));
                _alert.setTitle("提示信息");
                _alert.setHeaderText("关闭后默认关闭所有进程模拟！");
                Optional<ButtonType> result = _alert.showAndWait();
                if(result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES){
                    endSimulation();
                    stage.close();
                }
                windowEvent.consume();
                ((Stage) startButton.getScene().getWindow()).setAlwaysOnTop(true);
            }
        });

    }

    public void startSimulation(ActionEvent actionEvent) {
        if (this.comboBox.getSelectionModel().selectedItemProperty() == null) {
            this.comboBox.setTooltip(new Tooltip("在开始模拟之前，要选择开始模拟的可执行文件"));
        } else {
            ExecutableFile executableFile = (ExecutableFile)this.comboBox.getSelectionModel().getSelectedItem();
            SchedulingAction.startScheduling(this, executableFile);
            // 控制只能点击一次“开始模拟按钮”
            startButton.setDisable(true);
        }
        setMonitor();
    }

    public void endSimulation(){
        CPUThread.flag = false;
        ProcessAction.destroyAllProcess();
    }

    public TextField getRunningId() {
        return runningId;
    }

    public void setRunningId(String runningId) {
        this.runningId.setText(runningId);
    }

    public TextField getCurrentInstruction() {
        return currentInstruction;
    }

    public void setCurrentInstruction(String currentInstruction) {
//        this.currentInstruction.setText("");
        this.currentInstruction.setText(currentInstruction);
    }

    public TextField getIntermediateResult() {
        return intermediateResult;
    }

    public void setIntermediateResult(String intermediateResult) {
//        this.intermediateResult.setText("");
        this.intermediateResult.setText(intermediateResult);
    }

    public TextField getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime.setText(executionTime);
    }

    /**
     * 弹出一个信息对话框
     * @param p_header
     * @param p_message
     */
    public static void informationDialog(String p_header, String p_message){

        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("提示信息");
        _alert.setHeaderText(p_header);
        _alert.setContentText(p_message);
        _alert.showAndWait();

    }
}
