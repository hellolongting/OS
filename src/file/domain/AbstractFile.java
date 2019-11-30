package file.domain;

import file.driver.DiskDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AbstractFile
 * @Description 抽象文件类
 * @Date 2019/11/11
 **/
public abstract class AbstractFile {
    //文件名, 占用11Byte
    private String name;
    //扩展名, 占用1Byte,可执行文件为e
    private String extendName;
    //文件类型, 占用1Byte
    private FileType type;
    //文件属性, 占用1Byte
    private byte attribute;
    //起始盘块号, 占用1Byte
    private int number;
    //字节长度, 占用2Byte，文件最大长度65535Byte
    private int length;
    //父文件
    private Directory parent;
    //磁盘驱动
    private DiskDriver diskDriver = new DiskDriver();

    //是否只读,第一位
    public boolean IsReadOnly() {
        return (attribute & 1) == 1 ? true : false;
    }

    //是否系统文件,第二位
    public boolean IsSystemFile() {
        return (attribute >> 1 & 1) == 1 ? true : false;
    }

    //是否可写,第三位
    public boolean IsWritable() {
        return  (attribute >> 2 & 1) == 1 ? true : false;
    }

    //是目录还是文件,第四位
    public boolean IsDirectory() {
        return  (attribute >> 3 & 1) == 1 ? true : false;
    }

    public AbstractFile() {};

    public AbstractFile(String name, FileType type, boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory) {
        setName(name);
        setType(type);
        byte attribute = 0;
        if(isReadOnly)
            attribute |= 1;
        if(isSystemFile)
            attribute |= 2;
        if(isWritable)
            attribute |= 4;
        if(isDirectory)
            attribute |= 8;
        setAttribute(attribute);
    }

    /**
     * @desc 读取该文件的所有占有的磁盘块
     * @Date 2019/11/12
     * @param
     * @return java.util.List<file.domain.DiskChunk>
     **/
    public List<DiskChunk> read() {
        FAT fat = FileBuffer.fat;
        List<Integer> numberList = new ArrayList<>();
        int[] table = fat.getTable();
        int num = number;
        do {
            numberList.add(num);
            num = table[num];
        } while(num != 1);
        return diskDriver.read(numberList);
    }

    /**
     * @desc 将该文件的更新信息保存
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    public void updateParent() {
        FileBuffer.fat.writeBack();
        if(getParent() != null) {
            getParent().updateWrite();
        } else {
            //该文件是根目录,需要更新长度信息
            byte[] bytes = new byte[DiskDriver.DISK_CHUNK_SIZE];
            DiskChunk diskChunk = new DiskChunk(256, bytes);
            diskChunk.getContent()[0] = (byte) getLength();
            getDiskDriver().write(diskChunk);
        }
    }
    
    public String getURL() {
    	return type == FileType.ROOT ? "root" : getParent().getURL() + "/" + name;
    }

    //getter & setter
    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public byte getAttribute() {
        return attribute;
    }

    public void setAttribute(byte attribute) {
        this.attribute = attribute;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    public DiskDriver getDiskDriver() {
        return diskDriver;
    }

    public void setDiskDriver(DiskDriver diskDriver) {
        this.diskDriver = diskDriver;
    }
}
