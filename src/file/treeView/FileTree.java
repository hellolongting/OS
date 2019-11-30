package file.treeView;

import java.awt.event.MouseEvent;
import java.util.List;

import file.action.OpenTextFileAction;
import file.controller.FileController;
import file.domain.AbstractFile;
import file.domain.Directory;
import file.domain.FileBuffer;
import file.domain.FileType;
import file.model.Services;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class FileTree {
	//控制
	private static FileController fileController;
	//目录树构建
	private static TreeView<TreeNode> treeView;
	//当前选择的项
	private TreeItem<TreeNode> selectedItem;
	//根目录
	private TreeItem<TreeNode> rootItem;
	private ChangeListener<TreeItem<TreeNode>> e;
	
	
	public FileTree(FileController fileController,TreeView<TreeNode> tree) {
		this.fileController=fileController;
		this.treeView=tree;
		Directory root = FileBuffer.root;
		TreeNode rootNode = new TreeNode(root);
		rootItem = new TreeItem(rootNode);
		treeView.setRoot(rootItem);
		selectedItem = rootItem;
		rootItem.setExpanded(true);
		treeView.setEditable(true);

		readDic(rootItem);
		initTree();
	}
	
	public void initTree() {
		

		//动态加载目录树
		treeView.getSelectionModel().selectedItemProperty().addListener(e = new ChangeListener<TreeItem<TreeNode>>() {


			@Override
			public void changed(ObservableValue<? extends TreeItem<TreeNode>> observable, TreeItem<TreeNode> oldValue,
					TreeItem<TreeNode> newValue) {
				
//				Services.indexOfSelectTreeNode = treeView.getRow(newValue);
				selectedItem = newValue;
				Services.selectTreeNode = newValue;
				if(!newValue.isLeaf()&&newValue.getChildren().get(0)==null) {
					newValue.getChildren().remove(0);
					readDic(newValue);
				}
//				if(newValue.getValue().getFile().getType().equals(FileType.TEXT_FILE)) {
//					
//					fileController.open = new OpenTextFileAction();
//				}
			}

			
			
		});
		
		treeView.setCellFactory(new Callback<TreeView<TreeNode>,TreeCell<TreeNode>>(){
            @Override
            public TreeCell<TreeNode> call(TreeView<TreeNode> p) {
                return new TreeNodeCell(fileController);
            }
        }); 
	}
	
	//读取目录
	public void readDic(TreeItem<TreeNode> node) {
		Directory dic = (Directory)node.getValue().getFile();
		List<AbstractFile> list = dic.getItemList();
		if(list == null) {
			return;
		}
		for(AbstractFile file : list) {
			if(file.getType().equals(FileType.DIRECTORY)) {
				//Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("../images/main/pacakage.png")));
				TreeItem<TreeNode> init_file = new TreeItem(new TreeNode(file));
				TreeItem<TreeNode> child = null;
				
				ImageView i_1 = new ImageView("images/main/pacakage.png");
				init_file.setGraphic(i_1);
				init_file.getChildren().add(child);																									
				node.getChildren().add(init_file);
			}
			else if(file.getType().equals(FileType.TEXT_FILE)){
				TreeItem<TreeNode> init_file = new TreeItem(new TreeNode(file));
				ImageView i_1 = new ImageView(Services.textFileImage);
				init_file.setGraphic(i_1);
				node.getChildren().add(init_file);
			}
			else if(file.getType().equals(FileType.EXECUTABLE)) {
				TreeItem<TreeNode> init_file = new TreeItem(new TreeNode(file));
				ImageView i_1 = new ImageView(Services.exetFileImage);
				init_file.setGraphic(i_1);
				node.getChildren().add(init_file);
			}
		}
	}
	

	/*
	 * 返回当前的树视图
	 */
	public static TreeView<TreeNode> getTreeView() {
		return treeView;
	}
}
