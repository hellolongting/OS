package file.domain;

import file.driver.DiskDriver;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FileBuffer
 * @Description 文件缓存区
 * @Date 2019/11/11
 **/
public class FileBuffer {
    public static FAT fat = new FAT();
    public static Directory root;
    public static List<ExecutableFile> exeList;


    static {
        root = new Directory("root", FileType.ROOT, false, true, true, true);
        root.setNumber(DiskDriver.ROOT_LOCATION);
        root.setLength(Byte.toUnsignedInt(new DiskDriver().read(DiskDriver.DISK_TOTAL).getContent()[0]));
        //加载所有可执行文件
        exeList = new ArrayList<>();
        retrieveExecutableFile(root);
    }

    /**
     * @desc 检索所有可执行文件
     * @param d
     * @return void
     **/
    public static void retrieveExecutableFile(Directory d) {
        for(AbstractFile file: d.getItemList()) {
            if(file.getType() == FileType.EXECUTABLE) {
                exeList.add((ExecutableFile) file);
            } else if(file.getType() == FileType.DIRECTORY) {
                retrieveExecutableFile((Directory) file);
            }
        }
    }
}
