package file.domain;

import file.driver.DiskDriver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Directory
 * @Description 目录类
 * @Date 2019/11/12
 **/
public class Directory extends AbstractFile {
    //目录项
    private List<AbstractFile> itemList = null;

    public Directory() {};

    public Directory(String name, FileType type, boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory) {
        super(name, type, isReadOnly, isSystemFile, isWritable, isDirectory);
    }

    /**
     * @desc 获取目录项
     * @Date 2019/11/12
     * @param
     * @return java.util.List<file.domain.AbstractFile>
     **/
    public List<AbstractFile> getItemList() {
        //还未读入
        if(itemList == null) {
            System.out.println(getName());
            List<DiskChunk> list = super.read();
            itemList = new ArrayList<>();
            //遍历磁盘块
            for(int k = 0; k < list.size(); k++) {
                int size = DiskDriver.DISK_CHUNK_SIZE;
                //左边界
                int len = k * size;
                DiskChunk chunk = list.get(k);
                byte[] bytes = chunk.getContent();
                //遍历单个磁盘块的每个目录项
                for(int i = 0; i < size && i + len < getLength(); i += DiskDriver.DIR_ITEM_LENGTH) {
                    //每16Byte读为一个目录项
                    AbstractFile file = changeToFile(bytes, i, this);
                    itemList.add(file);
                }
            }
        }
        return itemList;
    }

    /**
     * @desc 添加子文件
     * @Date 2019/11/12
     * @param abstractFile
     * @return void
     **/
    public void addItem(AbstractFile abstractFile) {
        if(itemList == null) {
            getItemList();
        }
        itemList.add(abstractFile);
        //补充属性
        abstractFile.setParent(this);
        abstractFile.setLength(0);
        //申请一块磁盘块
        abstractFile.setNumber(FileBuffer.fat.getUnusedChunk());
        FileBuffer.fat.getTable()[abstractFile.getNumber()] = 1;
        //写回磁盘
        addWrite();
        //更新本目录所在目录信息（长度信息）
        updateParent();
        //追加可执行文件集
        if(abstractFile.getType() == FileType.EXECUTABLE) {
            FileBuffer.exeList.add((ExecutableFile) abstractFile);
        }
    }

    /**
     * @desc 更新本目录
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    public void updateItem() {
        if(itemList == null) {
            getItemList();
        }
        updateWrite();
        //更新本目录所在目录信息（长度信息）
        updateParent();
    }

    /**
     * @desc 删除子文件
     * @Date 2019/11/12
     * @param abstractFile
     * @return void
     **/
    public void deleteItem(AbstractFile abstractFile) {
        if(itemList == null) {
            getItemList();
        }
        itemList.remove(abstractFile);
        //删除该子文件所占空间
        releaseFile(abstractFile);
        //写入
        updateWrite();
        //更新本目录所在目录信息（长度信息）
        updateParent();
        //删除可执行文件
        if(abstractFile.getType() == FileType.EXECUTABLE) {
            FileBuffer.exeList.remove(abstractFile);
        }
    }

    /**
     * @desc 释放文件所占磁盘空间
     * @Date 2019/11/12
     * @param abstractFile
     * @return void
     **/
    private void releaseFile(AbstractFile abstractFile) {
        if(abstractFile.getType() == FileType.DIRECTORY) {
            Directory directory = (Directory) abstractFile;
            for(AbstractFile file : directory.getItemList()) {
                releaseFile(file);
            }
            FileBuffer.fat.release(abstractFile.getNumber());
        } else {
            FileBuffer.fat.release(abstractFile.getNumber());
        }
    }

    /**
     * @desc 将目录项转化为字节
     * @Date 2019/11/12
     * @param abstractFile
     * @return byte[]
     **/
    private byte[] changeToByte(AbstractFile abstractFile) {
        byte[] bytes = new byte[16];
        byte[] nameBytes = abstractFile.getName().getBytes();
        int i = 0;
        //前11个字节存储文件名,不足则补0
        for(; i < 11; i++) {
            if(i < nameBytes.length) {
                bytes[i] = nameBytes[i];
            } else {
                bytes[i] = 0;
            }
        }
        //文件类型
        bytes[11] = (byte) abstractFile.getType().getCode();
        //属性
        bytes[12] = abstractFile.getAttribute();
        //起始盘块
        bytes[13] = (byte) abstractFile.getNumber();
        //长度
        bytes[14] = (byte) (abstractFile.getLength()/DiskDriver.DISK_TOTAL);
        bytes[15] = (byte) (abstractFile.getLength()%DiskDriver.DISK_TOTAL);
        return bytes;
    }

    /**
     * @desc 将16字节转化为目录项,需设置文件名，类型，属性，起始盘块，长度，父目录
     * @Date 2019/11/17
     * @param bytes
     * @param i
     * @param parent
     * @return file.domain.AbstractFile
     **/
    private AbstractFile changeToFile(byte[] bytes, int i, Directory parent) {
        //文件类型
        FileType fileType = FileType.getValue(Byte.toUnsignedInt(bytes[i + 11]));
        AbstractFile file = null;
        if(fileType == FileType.DIRECTORY) {
            file = new Directory();
            file.setType(FileType.DIRECTORY);
        } else if(fileType == FileType.TEXT_FILE) {
            file = new TextFile();
            file.setType(FileType.TEXT_FILE);
        } else if(fileType == FileType.EXECUTABLE) {
            file = new ExecutableFile();
            file.setType(FileType.EXECUTABLE.EXECUTABLE);
        }
        //名称11字节
        file.setName(new String(bytes, i, 10).trim());
        //属性
        file.setAttribute(bytes[i + 12]);
        //盘块号
        file.setNumber(Byte.toUnsignedInt(bytes[i + 13]));
        //长度两字节
        file.setLength(Byte.toUnsignedInt(bytes[i + 14]) * 256 + Byte.toUnsignedInt(bytes[i + 15]));
        file.setParent(parent);
        return file;
    }

    /**
     * @desc 修改操作同步至磁盘：末尾追加
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    private void addWrite() {
        int num = getNumber();
        int size = DiskDriver.DISK_CHUNK_SIZE;
        int total = size;
        int[] table = FileBuffer.fat.getTable();
        //找到最后一块磁盘
        while(table[num] != 1) {
            num = table[num];
            total += size;
        }
        //取得需要写入的文件
        AbstractFile endFile = itemList.get(itemList.size()-1);
        byte[] bytes = changeToByte(endFile);
        //查看是否有剩余容量
        if(getLength() == 0 || getLength() % size != 0) {
            //取得最后一块磁盘块
            DiskChunk diskChunk = getDiskDriver().read(num);
            //最后一个磁盘块已存的字节数
            int end = size - (total - getLength());
            //补充新目录项
            for(int i = 0; i < 16; i++) {
                diskChunk.getContent()[end + i] = bytes[i];
            }
            //写回磁盘
            getDiskDriver().write(diskChunk);
        } else {
            //构造新磁盘块
            byte[] temp = new byte[size];
            for(int i = 0; i < 16; i++) {
                temp[i] = bytes[i];
            }
            //请求空闲块
            int diskNum = FileBuffer.fat.getUnusedChunk();
            //写入
            if(diskNum == -1) {
                return;
            }
            //更新fat
            FileBuffer.fat.getTable()[num] = diskNum;
            FileBuffer.fat.getTable()[diskNum] = 1;
            getDiskDriver().write(new DiskChunk(diskNum, temp));
        }
        //更新长度
        setLength(getLength() + DiskDriver.DIR_ITEM_LENGTH);
    }

    /**
     * @desc 修改操作同步至磁盘：整体覆盖
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    public void updateWrite() {
        //释放所有空间
        FAT fat = FileBuffer.fat;
        int num = getNumber();
        fat.release(num);
        //重新申请空间
        if(getType() == FileType.ROOT) {
            setNumber(2);
        } else {
            setNumber(fat.getUnusedChunk());
        }
        fat.getTable()[getNumber()] = 1;
        num = getNumber();
        byte[] bytes = new byte[DiskDriver.DISK_CHUNK_SIZE];
        int cur = 0;
        for (int i = 0; i < itemList.size(); i++) {
            //已写满一块磁盘块
            if(i != 0 && i % 8 == 0) {
                getDiskDriver().write(new DiskChunk(num, bytes));
                //重新申请空间
                int tmp = fat.getUnusedChunk();
                fat.getTable()[num] = tmp;
                num = tmp;
                bytes = new byte[DiskDriver.DISK_CHUNK_SIZE];
                cur = 0;
            }
            byte[] temp = changeToByte(itemList.get(i));
            for(int j = 0; j < DiskDriver.DIR_ITEM_LENGTH; j++) {
                bytes[cur++] = temp[j];
            }
        }
        //写入最后一块磁盘
        getDiskDriver().write(new DiskChunk(num, bytes));
        fat.getTable()[num] = 1;
        setLength(itemList.size() * 16);
    }
}
