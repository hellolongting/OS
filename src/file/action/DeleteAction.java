package file.action;

import java.io.IOException;

import file.model.Services;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DeleteAction {
	public DeleteAction() {
		delete();
	}
	
	public void delete() {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getClassLoader().getResource("file/view/deleteFile.fxml"));
			Scene scence = new Scene(root);
			Stage stage = new Stage();
			if(Services.deleteStage!=null) {//获取已有的stage
				stage = Services.deleteStage;
			}
			else {
				Services.deleteStage = stage;
			}
//			stage.setResizable();//设置不能改变大小
			stage.setTitle("删除确认");
			stage.setScene(scence);
			stage.setAlwaysOnTop(true);
			stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
