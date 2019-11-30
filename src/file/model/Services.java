package file.model;

import file.domain.AbstractFile;
import file.treeView.TreeNode;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Services {
	public static Stage addStage; //新建文件界面
	public static Stage deleteStage; //删除文件界面
	public static Stage renameStage; //重命名文件界面
	public static Stage openTextStage; //打开文本文件界面
	public static Stage openExeStage; //打开可执行文件界面
	public static TreeItem<TreeNode> selectTreeNode; //当前选择的树节点
	public static int indexOfSelectTreeNode; //当前选择的树节点的坐标
	public static AbstractFile fileBuffer = null; //文件缓存
	public static String dicImage = "images/main/pacakage.png";//目录类文件的图标路径
	public static String textFileImage = "images/main/doc.png";//文本类文件的图标路径
	public static String exetFileImage = "images/main/exeFile.png";//可执行类文件的图标路径
}
