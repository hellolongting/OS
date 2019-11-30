package file.controller;

import java.net.URL;
import java.util.ResourceBundle;

import file.domain.AbstractFile;
import file.domain.Directory;
import file.model.Services;
import file.tableView.FATTableView;
import file.treeView.FileTree;
import file.treeView.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;

public class DeleteActionController implements Initializable{
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	/*
	 * 删除
	 */
	@FXML
	public void delete() {
		TreeItem<TreeNode> node= Services.selectTreeNode;
		Directory parent = node.getValue().getFile().getParent();
		parent.deleteItem(node.getValue().getFile());
		int i=FileTree.getTreeView().getRow(node.getParent());
		node.getParent().getChildren().remove(Services.indexOfSelectTreeNode-i-1);
		Services.deleteStage.close();
		FATTableView.updateFat();
	}
	
	@FXML
	public void exit() {
		Services.deleteStage.close();
	}

}
