package file.action;

import java.io.IOException;

import file.model.Services;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class OpenTextFileAction {
	public OpenTextFileAction() {
		open();
	}
	
	public void open() {
		try {
			AnchorPane root = FXMLLoader.load(getClass().getClassLoader().getResource("file/view/openTextFile.fxml"));
			Scene scence = new Scene(root); 
			Stage stage = new Stage();
			if(Services.openTextStage!=null) {//获取已有的stage
				stage = Services.openTextStage;
			}
			else {
				Services.openTextStage = stage;
			}
//			stage.setResizable();//设置不能改变大小
//			stage.setTitle("");
			stage.setScene(scence);
			stage.setAlwaysOnTop(true);
			stage.show();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
