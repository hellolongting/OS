package equipment_process_storage;

import equipment_process_storage.memory.SystemArea;
import equipment_process_storage.memory.UserArea;
import equipment_process_storage.util.MemoryScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author superferryman
 * @desc 存储管理界面
 * @date 2019/11/24 15:44
 */
public class EPSController implements Initializable {
    @FXML
    private PieChart userArea;
    @FXML
    private PieChart systemArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userArea.setData(FXCollections.observableArrayList(
                new PieChart.Data("已用内存", 0),
                new PieChart.Data("空闲内存", 100)
        ));
        this.userArea.setLabelLineLength(15);
        this.userArea.setClockwise(true);
        this.userArea.setLabelsVisible(true);
        this.systemArea.setData(FXCollections.observableArrayList(
                new PieChart.Data("已用内存", 0),
                new PieChart.Data("空闲内存", 100)
        ));
        this.systemArea.setLabelLineLength(15);
        this.systemArea.setClockwise(true);
        this.systemArea.setLabelsVisible(true);
        MemoryScheduler.initController(this);
    }

    public void updateUserAreaChart() {
        Platform.runLater(() -> {
            this.userArea.setData(FXCollections.observableArrayList(
                    new PieChart.Data("已用内存", UserArea.getInstance().getUsed()),
                    new PieChart.Data("空闲内存", UserArea.getInstance().getMaxSize() - UserArea.getInstance().getUsed())
            ));
        });
    }

    public void updateSystemAreaChart() {
        Platform.runLater(() -> {
            this.systemArea.setData(FXCollections.observableArrayList(
                    new PieChart.Data("已用内存", SystemArea.getInstance().getUsed()),
                    new PieChart.Data("空闲内存", SystemArea.getInstance().getMaxSize() - SystemArea.getInstance().getUsed())
            ));
        });
    }
}
