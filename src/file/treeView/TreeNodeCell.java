package file.treeView;

import java.util.List;

import file.action.AddAction;
import file.action.DeleteAction;
import file.action.OpenExecuteFileAction;
import file.action.OpenTextFileAction;
import file.action.RenameAction;
import file.controller.FileController;
import file.domain.AbstractFile;
import file.domain.Directory;
import file.domain.ExecutableFile;
import file.domain.FileBuffer;
import file.domain.FileType;
import file.domain.TextFile;
import file.driver.DiskDriver;
import file.model.Services;
import file.tableView.FATTableView;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public final class TreeNodeCell extends TreeCell<TreeNode>{
	private static FileController filecontroller;
	private AbstractFile file;
	private ContextMenu directoryMenu = new ContextMenu();
	private ContextMenu textMenu = new ContextMenu();
	private ContextMenu excuteMenu = new ContextMenu();
	private ContextMenu rootMenu = new ContextMenu();
	MenuItem paste = new MenuItem("粘贴");
	private boolean flag = true;
	public TreeNodeCell(FileController filecontroller) {
		this.filecontroller=filecontroller;
		dicAction();
		exeAction();
		txtAction();
		rootAction();
//		getTreeItem().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
//			if(e.getButton()==MouseButton.SECONDARY) {
//				Services.selectTreeNode = getTreeItem();
//				filecontroller.open = new OpenTextFileAction();
//			}
//		});
	}
	public void rootAction() {
//		MenuItem remake = new MenuItem("格式化磁盘");
//		rootMenu.getItems().add(remake);
//		remake.setOnAction(e->{
//			DiskDriver disk = new DiskDriver();
//			disk.initialDisk();
//		});
	}
	/*
	 * 给目录增加目录
	 */
	public void dicAction() {
		MenuItem add = new MenuItem("新建文件");
		MenuItem delete = new MenuItem("删除文件夹");
		MenuItem rename = new MenuItem("重命名");
		MenuItem copy = new MenuItem("复制");
		directoryMenu.getItems().addAll(add, delete, rename, copy);
		
		
		add.setOnAction(e->{
			System.out.println("添加");
			Services.selectTreeNode=getTreeItem();
			filecontroller.add = new AddAction();
		
		});
		
		delete.setOnAction(e->{
			System.out.println("删除");
			Services.selectTreeNode=getTreeItem();
			Services.indexOfSelectTreeNode = FileTree.getTreeView().getRow(getTreeItem());
			filecontroller.delete = new DeleteAction();
		
		});
		
		copy.setOnAction(e->{
			System.out.println("复制");
			Services.fileBuffer = getTreeItem().getValue().getFile();

		});
		
		rename.setOnAction(e->{
			Services.selectTreeNode=getTreeItem();
			filecontroller.rename = new RenameAction();
		});
		
		paste.setOnAction(e->{
			pasteFile();
			Services.fileBuffer=null;

		});

		
	}
	/*
	 * 
	 */
	public void exeAction() {
		 MenuItem execute = new MenuItem("运行");
		 MenuItem open = new MenuItem("编辑");
		 MenuItem delete = new MenuItem("删除文件");
		 MenuItem rename = new MenuItem("重命名");
		 MenuItem copy = new MenuItem("复制");
		 excuteMenu.getItems().addAll(execute,open,delete,rename,copy);
		 	execute.setOnAction(e->{
		 		Services.selectTreeNode = getTreeItem();
		 		
		 	});
		 	open.setOnAction(e->{
				Services.selectTreeNode = getTreeItem();
				filecontroller.openExe = new OpenExecuteFileAction();

			});
			
			delete.setOnAction(e -> {
				System.out.println("删除");
				Services.selectTreeNode = getTreeItem();
				Services.indexOfSelectTreeNode = FileTree.getTreeView().getRow(getTreeItem());
				filecontroller.delete = new DeleteAction();

			});
			
			rename.setOnAction(e->{
				Services.selectTreeNode=getTreeItem();
				filecontroller.rename = new RenameAction();
			});
			
			copy.setOnAction(e->{
				System.out.println("复制");
				Services.fileBuffer = getTreeItem().getValue().getFile();

			});
			
			paste.setOnAction(e->{
				pasteFile();
				Services.fileBuffer=null;

			});
	}
	
	public void txtAction() {
		MenuItem open = new MenuItem("打开");
		MenuItem delete = new MenuItem("删除文件");
		MenuItem rename = new MenuItem("重命名");
		MenuItem copy = new MenuItem("复制");
		textMenu.getItems().addAll(open, delete, rename, copy);

		open.setOnAction(e->{
			Services.selectTreeNode = getTreeItem();
			filecontroller.open = new OpenTextFileAction();

		});
		
		delete.setOnAction(e -> {
			System.out.println("删除");
			Services.selectTreeNode = getTreeItem();
			Services.indexOfSelectTreeNode = FileTree.getTreeView().getRow(getTreeItem());
			filecontroller.delete = new DeleteAction();

		});
		
		copy.setOnAction(e->{
			System.out.println("复制");
			Services.fileBuffer = getTreeItem().getValue().getFile();

		});
		
		rename.setOnAction(e->{
			Services.selectTreeNode=getTreeItem();
			filecontroller.rename = new RenameAction();
		});
		
		paste.setOnAction(e->{
			pasteFile();
			Services.fileBuffer=null;

		});
		
		
	}
	
	@Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        
    }

    @Override
    public void updateItem(TreeNode item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                
                setText(null);
             
            } else {
                setText(getTreeItem().getValue().toString());
                setGraphic(getTreeItem().getGraphic());
        		if(Services.fileBuffer!=null&&flag==true) {
        			directoryMenu.getItems().add(paste);
        		
        			flag=false;
        		}
        		else if(Services.fileBuffer==null){
        			flag=true;
        			directoryMenu.getItems().remove(paste);
        		}
        		if(getTreeItem().getValue().isDirectory()&&getTreeItem().getParent()==null) {
        			setContextMenu(rootMenu);
//        			System.out.println("root");
        		}
        		else if (!getTreeItem().isLeaf()&&getTreeItem().getParent()!= null&&getTreeItem().getValue().getFile().getType()==FileType.DIRECTORY){
                    setContextMenu(directoryMenu);
                }
                else if(getTreeItem().getValue().getFile().getType()==FileType.TEXT_FILE) {
                	setContextMenu(textMenu);
                }
                else if(getTreeItem().getValue().getFile().getType()==FileType.EXECUTABLE) {
                	setContextMenu(excuteMenu);
                }
            }
        }
    }
    /*
     * 粘贴
     * */
    public void pasteFile() {
    	Directory parent = (Directory) getTreeItem().getValue().getFile();
//    	AbstractFile file = Services.fileBuffer;
    	ImageView Image = null ;
    	
    	if(Services.fileBuffer.getType().equals(FileType.TEXT_FILE)) {
    		TextFile file = (TextFile)Services.fileBuffer;
    		Image = new ImageView(Services.textFileImage);
    		String fileName = file.getName();
    		//System.out.println(fileName);
    		int num = isSame(parent,file);
    		//System.out.println(num+"***");
    		if(num>=1) {//判断是否有相同名字
    			String[] s = fileName.split("\\.");
    			StringBuffer str = new StringBuffer();
//    			System.out.println(s[0]);
    			str.append(s[0]);
    			str.append("(");
    			str.append(num);
    			str.append(").");
    			str.append(s[1]);
    			fileName = str.toString();
//    			System.out.println(s[1]);
    			//System.out.println(fileName);
    		}
    		
    		
    		TextFile newFile = new TextFile(fileName,file.getType(),file.IsReadOnly(),file.IsSystemFile(),file.IsWritable(),file.IsDirectory());
//    				 boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory
//    		System.out.println("error1");
    		parent.addItem(newFile);
    		
//    		System.out.println("error2");
    		newFile.setText(file.getText());
//    		newFile.saveText();
//    		System.out.println("error3");
    		newFile.saveText();
//    		System.out.println("error4");
    		TreeNode node = new TreeNode(newFile);
//    		System.out.println("error5");
    		Services.selectTreeNode.getChildren().add(new TreeItem(node,Image));
//    		System.out.println("error6");
    	}
    	else if(Services.fileBuffer.getType().equals(FileType.DIRECTORY)) {
    		Directory file = (Directory)Services.fileBuffer;
    		Image = new ImageView(Services.dicImage);
    		String fileName = file.getName();
    		int num = isSame(parent,file);
    		if(num>=1) {//判断是否有相同名字
    			String[] s = fileName.split("\\(");
    			StringBuffer str = new StringBuffer();
    			str.append(s[0]);
    			str.append("(");
    			str.append(num);
    			str.append(")");
    			fileName = str.toString();
    			//System.out.println(s[0]);
    			//System.out.println(fileName);
    		}
    		Directory newFile = new Directory(fileName,file.getType(),file.IsReadOnly(),file.IsSystemFile(),file.IsWritable(),file.IsDirectory());
    		parent.addItem(newFile);
    		copyFile(file,newFile);
    		
    		
    		TreeNode node = new TreeNode(newFile);
    		TreeItem item=new TreeItem(node,Image);
    		item.getChildren().add(null);
    		Services.selectTreeNode.getChildren().add(item);
    		
    	}
    	else if(Services.fileBuffer.getType().equals(FileType.EXECUTABLE)) {
    		ExecutableFile file = (ExecutableFile)Services.fileBuffer;
    		Image = new ImageView(Services.exetFileImage);
    		String fileName = file.getName();
    		int num = isSame(parent,file);
    		if(num>=1) {//判断是否有相同名字
    			String[] s = fileName.split("\\.");
    			StringBuffer str = new StringBuffer();
    			str.append(s[0]);
    			str.append("(");
    			str.append(num);
    			str.append(").");
    			str.append(s[1]);
    			fileName = str.toString();
    			System.out.println(s[0]);
    			System.out.println(fileName);
    		}
    		ExecutableFile newFile = new ExecutableFile(fileName,file.getType(),file.IsReadOnly(),file.IsSystemFile(),file.IsWritable(),file.IsDirectory());
//    				 boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory
			parent.addItem(newFile);
    		newFile.setCode(file.getCode());
    		newFile.saveCode();
    		
    		TreeNode node = new TreeNode(newFile);
    		Services.selectTreeNode.getChildren().add(new TreeItem(node,Image));
    	}
    	FATTableView.updateFat();
    }
    /*
     * 判断是否有相同名字
     */
    private int isSame(Directory parent,AbstractFile file) {
    	int flag=0;
    	List<AbstractFile> list = parent.getItemList();
    	String s0 = file.getName();
    	String[] s = file.getName().split("\\.");
    	String[] s1 =s0.split("\\(");
    	//System.out.println(s[0]);
    	//System.out.println(s1[0]);
    	for(AbstractFile f:list) {
    		if(f.getName().indexOf(s[0])!=-1||f.getName().indexOf(s1[0])!=-1) {
    			flag=flag+1;
    			//System.out.println("1+++");
    		}
    		
    	}
    	
    	return flag;
    }
	/*
	 * 递归复制文件
	 */
    private void copyFile(Directory parent,Directory clone) {
   
    	for(AbstractFile f : parent.getItemList()) {
    		if(f.getType().equals(FileType.DIRECTORY)) {
    			Directory cloneDic = new Directory(f.getName(),f.getType(),f.IsReadOnly(),f.IsSystemFile(),f.IsWritable(),f.IsDirectory());
    			clone.addItem(cloneDic);
    			copyFile((Directory)f,cloneDic);
    		}
    		else if(f.getType().equals(FileType.TEXT_FILE)) {
    			TextFile newFile = new TextFile(f.getName(),f.getType(),f.IsReadOnly(),f.IsSystemFile(),f.IsWritable(),f.IsDirectory());
    			clone.addItem(newFile);
    			newFile.setText(((TextFile)f).getText());
    			newFile.saveText();
    		}
    		else {
    			ExecutableFile newFile = new ExecutableFile(f.getName(),f.getType(),f.IsReadOnly(),f.IsSystemFile(),f.IsWritable(),f.IsDirectory());
    			clone.addItem(newFile);
    			newFile.setCode(((ExecutableFile)f).getCode());
    			newFile.saveCode();
    		}
    		
    		
    	}
    }
    
    
    
}
