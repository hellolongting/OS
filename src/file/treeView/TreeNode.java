package file.treeView;

import file.domain.AbstractFile;

public class TreeNode {
	private String fileName;
	private AbstractFile file;
	private String fileRoad;
	public TreeNode(AbstractFile file) {
		this.fileName = file.getName();
		this.file=file;
		this.fileRoad = file.getURL();
	}
	
	
	
	public String getFileName() {
		return fileName;
	}



	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public AbstractFile getFile() {
		return file;
	}



	public void setFile(AbstractFile file) {
		this.file = file;
	}



	public String getFileRoad() {
		return fileRoad;
	}



	public void setFileRoad(String fileRoad) {
		this.fileRoad = fileRoad;
	}



	public boolean isDirectory() {
		return file.IsDirectory();
	}
	
	public boolean IsReadOnly() {
		return file.IsReadOnly();
	}
	
	public boolean IsSystemFile() {
		return file.IsSystemFile();
	}
	
	public boolean IsWritable() {
		return file.IsWritable();
	}
	
	/*
	 * 获取生成目录树节点的名称
	 */
	@Override
	public String toString() {
		return file.getName();
	}
	
}
