package file.domain;

import file.driver.DiskDriver;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileBuffer
 * @Description 文件缓存区
 * @Date 2019/11/11
 **/
public class FileBuffer {
    public static FAT fat;
    public static Directory root;
    public static List<ExecutableFile> exeList;


    static {
        File file = new File("./disk");
        boolean flag = file.exists();
        if(flag == false) {
            new DiskDriver().initialDisk();
        }
        fat = new FAT();
        root = new Directory("root", FileType.ROOT, false, true, true, true);
        root.setNumber(DiskDriver.ROOT_LOCATION);
        root.setLength(Byte.toUnsignedInt(new DiskDriver().read(DiskDriver.DISK_TOTAL).getContent()[0]));
        if(flag == false) {
            root.addItem(new Directory("dir1", FileType.DIRECTORY, false, true, true, true));
            root.addItem(new Directory("dir2", FileType.DIRECTORY, false, true, true, true));
            root.addItem(new Directory("dir3", FileType.DIRECTORY, false, true, true, true));
            root.addItem(new Directory("dir4", FileType.DIRECTORY, false, true, true, true));
            root.addItem(new Directory("dir5", FileType.DIRECTORY, false, true, true, true));
            root.addItem(new Directory("dir6", FileType.DIRECTORY, false, true, true, true));
        }
        //加载所有可执行文件
        exeList = new ArrayList<>();
        retrieveExecutableFile(root);
    }

    /**
     * @desc 检索所有可执行文件
     * @param d
     * @return void
     **/
    private static void retrieveExecutableFile(Directory d) {
        for(AbstractFile file: d.getItemList()) {
            if(file.getType() == FileType.EXECUTABLE) {
                exeList.add((ExecutableFile) file);
            } else if(file.getType() == FileType.DIRECTORY) {
                retrieveExecutableFile((Directory) file);
            }
        }
    }
}
