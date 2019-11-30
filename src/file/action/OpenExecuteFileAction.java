package file.action;

import java.io.IOException;

import file.model.Services;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class OpenExecuteFileAction {
	public OpenExecuteFileAction() {
		open();
	}
	
	public void open() {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getClassLoader().getResource("file/view/openExeFile.fxml"));
			Scene scence = new Scene(root); 
			Stage stage = new Stage();
			if(Services.openExeStage!=null) {//获取已有的stage
				stage = Services.openExeStage;
			}
			else {
				Services.openExeStage = stage;
			}
//			stage.setResizable();//设置不能改变大小
//			stage.setTitle("");
			stage.setScene(scence);
			stage.setAlwaysOnTop(true);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
