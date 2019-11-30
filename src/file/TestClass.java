package file;

import file.domain.*;
import file.driver.DiskDriver;

import java.util.List;


/**
 * @ClassName Test
 * @Description
 * @Date 2019/11/12
 **/
public class TestClass {

    public void printfFat() {
        FAT fat = FileBuffer.fat;
        int cnt = 0;
        for(int i = 0; i < fat.getTable().length; i++) {
            if(fat.getTable()[i] == 0) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }

    public void test() {
        //根目录
        Directory root = FileBuffer.root;
        //获取根目录下的所有文件
        List<AbstractFile> list = root.getItemList();
        //在根目录下添加子目录
        root.addItem(new Directory("目录1", FileType.DIRECTORY, true, true, true, true));
        //在根目录下添加文本文件
        TextFile textFile = new TextFile("文本1", FileType.TEXT_FILE, true, true, true, true);
        root.addItem(textFile);
        //修改文本文件内容
        textFile.setText("hello world");
        textFile.saveText();


    }

    public void initRoot() {
        Directory root = FileBuffer.root;
        Directory d1 = (Directory) root.getItemList().get(0);
        d1.addItem(new Directory("目录-1", FileType.DIRECTORY, false, true, true, true));

    }

    public void init(String[] args) {
        new DiskDriver().initialDisk();
    }

    public static void main(String[] args) {
       new DiskDriver().initialDisk();
    }
}
