package file.controller;

import java.net.URL;
import java.util.ResourceBundle;

import file.domain.AbstractFile;
import file.domain.Directory;
import file.domain.ExecutableFile;
import file.domain.FileType;
import file.domain.TextFile;
import file.model.Services;
import file.tableView.FATTableView;
import file.treeView.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class AddActionController implements Initializable{

	private String filename=null;
	private FileType type=null;
	@FXML
	private TextField text;
	@FXML
	private ComboBox<String> cb;
	@FXML
	private Text URL;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		cb.getItems().clear();
		cb.getItems().add("目录");
		cb.getItems().add("文本文件");
		cb.getItems().add("可执行文件");
		cb.valueProperty().addListener(e->{
			if(cb.getValue().equals("目录")) {
				type = FileType.DIRECTORY;
			}
			else if(cb.getValue().equals("文本文件")) {
				type = FileType.TEXT_FILE;
			}
			else {
				type = FileType.EXECUTABLE;
			}
		});
		URL.setText(Services.selectTreeNode.getValue().getFile().getURL());
	}
	
	@FXML
	private void importFileName() {
		AbstractFile file = null;
		ImageView Image = null ;
		Directory parent = (Directory)Services.selectTreeNode.getValue().getFile();
		
		this.filename = text.getText();
		System.out.println(this.filename.length()+"***");
		if(filename.length()==0) {
			System.out.println("111**");
			Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("文件名不能为空");
            alert.initOwner(Services.addStage);
            alert.showAndWait();
            return;
		}
		/*
		 * 判断文件属性，给予不同的创建方式
		 */
		if(type == FileType.DIRECTORY) {
			file = new Directory(filename, FileType.DIRECTORY, false, true, true, true);
			parent.addItem(file);
			Image = new ImageView(Services.dicImage);
			TreeNode node = new TreeNode(file);
			Services.selectTreeNode.getChildren().add(new TreeItem(node,Image));
			System.out.println(filename);
			Services.addStage.close();
			FATTableView.updateFat();
		}
		else if(type == FileType.TEXT_FILE) {
			file = new TextFile(filename+".t", FileType.TEXT_FILE, false, true, true, true);
			parent.addItem(file);
			Image = new ImageView(Services.textFileImage);
			TreeNode node = new TreeNode(file);
			Services.selectTreeNode.getChildren().add(new TreeItem(node,Image));
			System.out.println(filename);
			Services.addStage.close();
			FATTableView.updateFat();
		}
		else if(type == FileType.EXECUTABLE) {
			file = new ExecutableFile(filename+".e", FileType.EXECUTABLE, false, true, true, true);
			parent.addItem(file);
			Image = new ImageView(Services.exetFileImage);
			TreeNode node = new TreeNode(file);
			Services.selectTreeNode.getChildren().add(new TreeItem(node,Image));
			System.out.println(filename);
			Services.addStage.close();
			FATTableView.updateFat();
		}
		
	}
	
	@FXML
	private void exit() {
		Services.addStage.close();
	}
}
