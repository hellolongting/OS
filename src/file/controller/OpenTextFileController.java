package file.controller;

import java.net.URL;
import java.util.ResourceBundle;

import file.domain.TextFile;
import file.model.Services;
import file.tableView.FATTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class OpenTextFileController implements Initializable{

	private TextFile textFile;
	@FXML
	private TextArea text;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		textFile = (TextFile)Services.selectTreeNode.getValue().getFile();
//		Services.openTextStage.setTitle(textFile.getName()+".txt");
		String txt = textFile.getText();
		text.appendText(txt);
	}
	/*
	 * 保存文本文件
	 * */
	@FXML
	public void save() {
		String txt = text.getText();
		textFile.setText(txt);
		if(textFile.IsWritable()) {
			textFile.saveText();
			FATTableView.updateFat();
		}
		
	}
	/*
	 * 退出
	 */
	@FXML
	public void exit() {
		Services.openTextStage.close();
	}
	
}
