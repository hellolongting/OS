package file.action;

import java.io.IOException;

import file.model.Services;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddAction {
	
	public AddAction() {
		add();
	}
	
	public void add() {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getClassLoader().getResource("file/view/addFile.fxml"));
			Scene scence = new Scene(root);
			Stage stage = new Stage();
			if(Services.addStage!=null) {//获取已有的stage
				stage = Services.addStage;
			}
			else {
				Services.addStage = stage;
			}
//			stage.setResizable();//设置不能改变大小
			stage.setTitle("创建文件");
			stage.setScene(scence);
			stage.setAlwaysOnTop(true);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
