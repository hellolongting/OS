package file.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import file.domain.AbstractFile;
import file.domain.Directory;
import file.domain.FileType;
import file.model.Services;
import file.treeView.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class RenameActionController implements Initializable{
	/*
	 * 输入新文件名
	 */
	@FXML
	TextField text;
	
	AbstractFile file=null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		file = Services.selectTreeNode.getValue().getFile();
		System.out.println(file.getName());
		text.setPromptText(file.getName());
	}
	/*
	 * 创建确认
	 */
	@FXML
	public void sure() {
		String newName = text.getText();
		String rr =null;
		if(file.getType().equals(FileType.EXECUTABLE)) 
			rr = ".e";
		else if(file.getType().equals(FileType.TEXT_FILE))
			rr=".t";
		newName = newName+rr;
		if(isSame(file,newName)) {
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("信息");
            alert.headerTextProperty().set("已有相同名字的文件");
            alert.initOwner(Services.renameStage);
            alert.showAndWait();

		}
		else {
			
			
			
			file.setName(newName);
			file.getParent().updateItem();
	//		Services.selectTreeNode.getValue();
		
			TreeNode newNode =new  TreeNode(file);
			Services.selectTreeNode.setValue(newNode);
			Services.renameStage.close();
		}
		
	}
	/*
	 * 退出
	 */
	@FXML
	public void exit() {
		Services.renameStage.close();
	}
	/*
	 * 判断再同一目录下是否有相同的文件
	 */
	public boolean isSame(AbstractFile file,String name) {
		boolean flag=false;
//		int num=0;
		Directory parent = (Directory)file.getParent();
		List<AbstractFile> list = parent.getItemList();
		for(AbstractFile f:list) {
			if(f.getName().equals(name)) {
				flag=true;
				break;
			}
		}
		
//		System.out.println(num);
		return flag;
	}

}
