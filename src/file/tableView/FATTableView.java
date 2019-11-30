package file.tableView;

import file.controller.FileController;
import file.domain.FAT;
import file.domain.FileBuffer;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/*
 * 右侧界面Table的显示
 */
public class FATTableView {
	public static TableView<Table> tableView;  
	private static FileController fileController;
	
	private FAT fat = FileBuffer.fat;
	public FATTableView(FileController fileController,TableView<Table> table) {
		FATTableView.fileController=fileController;
		this.tableView=table;
//		tableView.getColumns().removeAll();
		
		TableColumn DickChunkCol = tableView.getColumns().get(0);
		DickChunkCol.setMinWidth(100);
		DickChunkCol.setCellValueFactory(new PropertyValueFactory<>("index"));
		TableColumn UsedCol = tableView.getColumns().get(1);
		UsedCol.setMinWidth(100);
		UsedCol.setCellValueFactory(new PropertyValueFactory<>("used"));

		updateFat();
//		initTable();
	}
	
//	
//	private void initTable() {
//		tableView.getItems().removeAll();
//		int[] table = fat.getTable();
//		for(int i=0;i<table.length;i++) {
//			String used = "未使用";
//			if(table[i]!=0) {
//				 used = "已使用";
//			}
//					
//			Table table1 = new Table(i,used);
//			tableView.getItems().add(table1);
//		}
//	}
	
	public static void updateFat() {
		fileController.tableView.getItems().clear();
		System.out.println(tableView.getItems().size());
		int[] table = FileBuffer.fat.getTable();
		for(int i=0;i<table.length;i++) {
			String used = "未使用";
			if(table[i]!=0) {
				 used = "已使用";
			}	
			Table table1 = new Table(i,used);
			fileController.tableView.getItems().add(table1);
		}
	}
	
	
}
