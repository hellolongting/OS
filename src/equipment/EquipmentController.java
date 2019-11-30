package equipment;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import equipment.domain.EquipmentEnum;
import equipment.driver.EquipmentDriver;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import process.action.ProcessAction;
import process.action.SchedulingAction;
import process.domain.MyProcess;

/**
 * @author longting
 */
public class EquipmentController implements Initializable {
	@FXML
	private TextField A1_status, A2_status, B1_status, B2_status, B3_status, C1_status, C2_status, C3_status;
	@FXML
	private TextField A1_process, A2_process, B1_process, B2_process, B3_process, C1_process, C2_process, C3_process;
	@FXML
	private TextField A1_time, A2_time, B1_time, B2_time, B3_time, C1_time, C2_time, C3_time;
	
    public EquipmentController() {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	A1_status.setText("空闲");
    	A2_status.setText("空闲");
    	B1_status.setText("空闲");
    	B2_status.setText("空闲");
    	B3_status.setText("空闲");
    	C1_status.setText("空闲");
    	C2_status.setText("空闲");
    	C3_status.setText("空闲");
    	
    	EquipmentDriver.init(this);
    }
    
	
    //分配设备
    public void setEquipmentInfo(String status, String process, String time, String type) {
		switch (type) {
		case "A1":
			this.A1_status.setText(status);
			this.A1_process.setText(process);
			this.A1_time.setText(time);
			break;
		case "A2":
			this.A2_status.setText(status);
			this.A2_process.setText(process);
			this.A2_time.setText(time);
			break;
		case "B1":
			this.B1_status.setText(status);
			this.B1_process.setText(process);
			this.B1_time.setText(time);
			break;
		case "B2":
			this.B2_status.setText(status);
			this.B2_process.setText(process);
			this.B2_time.setText(time);
			break;
		case "B3":
			B3_status.setText(status);
			B3_process.setText(process);
			B3_time.setText(time);
			break;
		case "C1":
			C1_status.setText(status);
			C1_process.setText(process);
			C1_time.setText(time);
			break;
		case "C2":
			C2_status.setText(status);
			C2_process.setText(process);
			C2_time.setText(time);
			break;
		case "C3":
			C3_status.setText(status);
			C3_process.setText(process);
			C3_time.setText(time);
			break;
		default:break;
		}
    }
    
    // 设备倒计时
    public void setTime(String type,String time) {
		switch (type) {
		case "A1":
			this.A1_time.setText(time);
			break;
		case "A2":
			this.A2_time.setText(time);
			break;
		case "B1":
			this.B1_time.setText(time);
			break;
		case "B2":
			this.B2_time.setText(time);
			break;
		case "B3":
			B3_time.setText(time);
			break;
		case "C1":
			C1_time.setText(time);
			break;
		case "C2":
			C2_time.setText(time);
			break;
		case "C3":
			C3_time.setText(time);
			break;
		default:break;
		}
    }
}
