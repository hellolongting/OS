package file.controller;

import java.net.URL;
import java.util.ResourceBundle;

import file.domain.ExecutableFile;
import file.domain.TextFile;
import file.model.Services;
import file.tableView.FATTableView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class OpenExecuteFileController implements Initializable{
	private ExecutableFile exeFile;
	@FXML
	private TextArea text;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		exeFile = (ExecutableFile)Services.selectTreeNode.getValue().getFile();
//		Services.openTextStage.setTitle(textFile.getName()+".txt");
		String txt = exeFile.getCode();
		text.appendText(txt);
	}
	/*
	 * 保存文本文件
	 * */
	@FXML
	public void save() {
		String code = text.getText();
		exeFile.setCode(code);
		if(exeFile.IsWritable()) {
			exeFile.saveCode();
			FATTableView.updateFat();
		}
		
	}
	/*
	 * 退出
	 */
	@FXML
	public void exit() {
		Services.openExeStage.close();
	}
	
	/*
	 * 运行---
	 */
	@FXML
	public void run() {
		
	}
}
