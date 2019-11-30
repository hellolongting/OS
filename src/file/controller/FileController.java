package file.controller;

import java.net.URL;
import java.util.ResourceBundle;

import file.action.AddAction;
import file.action.DeleteAction;
import file.action.OpenExecuteFileAction;
import file.action.OpenTextFileAction;
import file.action.RenameAction;
import file.domain.FileBuffer;
import file.tableView.FATTableView;
import file.tableView.Table;
import file.treeView.FileTree;
import file.treeView.TreeNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;

public class FileController implements Initializable{

	private FileController fileController=this;
	@FXML
	public TreeView<TreeNode> treeView;
	@FXML
	public TableView<Table> tableView;
	public AddAction add;
	public DeleteAction delete;
	public OpenTextFileAction open;
	public OpenExecuteFileAction openExe;
	public RenameAction rename;
	private FileTree tree;
	private FATTableView table;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		tree = new FileTree(fileController,treeView);
		table = new FATTableView(fileController,tableView);
//		treeView=tree.getTreeView();
	}
	
	
}
