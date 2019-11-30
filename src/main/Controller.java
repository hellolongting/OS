package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.thread.ClockThread;
import util.SingletonStageUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import static javafx.stage.StageStyle.UNDECORATED;

/**
 * @author longting
 */
public class Controller implements Initializable{

    public Button close;
//    public Label thumbnail_file;
//    public Label thumbnail_eps;
    public Label clock;

    /**
    双击打开文件管理新窗口
     */
    public void file_clicked(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() >= 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            Stage fileStage = SingletonStageUtil.getFileStage();
            Parent root = FXMLLoader.load(getClass().getResource("../file/view/file.fxml"));
            fileStage.setTitle("文件管理");
            fileStage.setScene(new Scene(root));
            /* 始终显示在其他窗口之上 */
            fileStage.setAlwaysOnTop(true);
            fileStage.show();
//            fileStageMonitoring();
        }
    }

    /**
     双击打开设备、进程、设备管理新窗口
     */
    public void equipment_clicked(MouseEvent mouseEvent) throws IOException {
        if(mouseEvent.getClickCount() >= 2 && mouseEvent.getButton() == MouseButton.PRIMARY) {
            Stage epsStage = SingletonStageUtil.getEpsStage(this);
            Parent root = FXMLLoader.load(getClass().getResource("../equipment_process_storage/EPS.fxml"));
            epsStage.setTitle("设备、进程、设备管理");
            epsStage.setScene(new Scene(root));
            /* 始终显示在其他窗口之上 */
            epsStage.setAlwaysOnTop(true);
            epsStage.show();
//            epsStageMonitoring();
        }
    }

    public void win_close(ActionEvent actionEvent) throws IOException {
        Stage exit = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../exit/exit.fxml"));
        exit.setScene(new Scene(root));
        exit.initStyle(UNDECORATED);
        /* 始终显示在其他窗口之上 */
        exit.setAlwaysOnTop(true);
        exit.show();
    }
/*
    private void fileStageMonitoring() {
        SingletonStageUtil.fileStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                thumbnail_file.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }
        });
    }

    private void epsStageMonitoring() {
        SingletonStageUtil.epsStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                thumbnail_eps.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            }
        });
    }
*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * 记录当前开机时间
         */
        ClockThread clockThread = new ClockThread(this.clock);
        clockThread.start();

    }
}