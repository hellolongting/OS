package file.driver;

import file.domain.DiskChunk;
import file.domain.FAT;
import file.domain.FileBuffer;
import file.exception.InvalidBoundException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @ClassName DiskDriver
 * @Description 磁盘驱动
 * @Date 2019/11/11
 **/
public class DiskDriver {
    Logger logger = Logger.getLogger(DiskDriver.class.getName());

    //盘块起始位置（除去FAT及根目录）
    //1-2为FAT表,共256字节，可以保存256个盘块
    //3为根目录
    public static final int DISK_BEGIN = 3;
    public static final int ROOT_LOCATION = 2;
    //磁盘位置
    public static final String DISK_LOCATION = "./disk";
    //磁盘块大小/字节
    public static final int DISK_CHUNK_SIZE = 128;
    //磁盘块数0-255
    public static final int DISK_TOTAL = 256;
    //目录项长度
    public static final int DIR_ITEM_LENGTH = 16;

    /**
     * @desc 初始化磁盘,全部置0
     * @Date 2019/11/11
     * @return void
     **/
    public void initialDisk() {
        File file = new File(DISK_LOCATION);
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            logger.info("初始化磁盘失败，无法创建磁盘文件");
            return;
        }
        //初始化FAT
        initFAT();
        //初始化根目录
        initRoot();
    }

    /**
     * @desc 读取指定磁盘块
     * @Date 2019/11/11
     * @param number
     * @return file.domain.DiskChunk
     **/
    public DiskChunk read(int number) {
        //块号是否合法
        if(number < 0 || number > DISK_TOTAL) {
            new InvalidBoundException("读取磁盘块范围不合法");
        }
        RandomAccessFile in = createAccessFile();
        //确定读取位置
        long location = number * DISK_CHUNK_SIZE;
        byte[] bytes = new byte[DISK_CHUNK_SIZE];
        try {
            in.seek(location);
            in.read(bytes, 0, DISK_CHUNK_SIZE);
        } catch (IOException e) {
            logger.info("读取磁盘块错误...");
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.info("文件关闭失败...");
                e.printStackTrace();
            }
        }
        return new DiskChunk(number, bytes);
    }

    public List<DiskChunk> read(List<Integer> list) {
        List<DiskChunk> diskChunkList = new ArrayList<>();
        list.forEach(i -> {
            DiskChunk diskChunk = read(i);
            diskChunkList.add(diskChunk);
        });
        return diskChunkList;
    }

    /**
     * @desc 写入指定磁盘块
     * @Date 2019/11/11
     * @param diskChunk
     * @return void
     **/
    public void write(DiskChunk diskChunk) {
        RandomAccessFile out = createAccessFile();
        long location = diskChunk.getNumber() * DISK_CHUNK_SIZE;
        try {
            out.seek(location);
            out.write(diskChunk.getContent(), 0, DISK_CHUNK_SIZE);
        } catch (IOException e) {
            logger.info("写入磁盘块出错...");
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.info("文件关闭失败...");
                e.printStackTrace();
            }
        }
    }

    public void write(List<DiskChunk> list) {
        list.forEach(diskChunk -> {
            write(diskChunk);
        });
    }

    /**
     * @desc 随机访问
     * @Date 2019/11/11
     * @param
     * @return java.io.RandomAccessFile
     **/
    private RandomAccessFile createAccessFile() {
        try {
            return new RandomAccessFile(DISK_LOCATION, "rw");
        } catch (FileNotFoundException e) {
            logger.info("访问的磁盘路径不存在...");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @desc 初始化根目录
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    public void initRoot() {
        //记录根目录大小的盘块,初始为0
        byte[] bytes = new byte[DISK_CHUNK_SIZE];
        bytes[0] = 0;
        //放在最后+1块
        DiskChunk diskChunk = new DiskChunk(DISK_TOTAL, bytes);
        write(diskChunk);
    }

    /**
     * @desc  初始化FAT
     * @Date 2019/11/17
     * @param
     * @return void
     **/
    public void initFAT() {
        FAT fat = FileBuffer.fat;
        int[] table = new int[DiskDriver.DISK_TOTAL];
        //初始化分配表,0-2块盘块已占用,其余空闲
        for(int i = 0; i < DISK_TOTAL; i++) {
            table[i] = i < DISK_BEGIN ? 1 : 0;
        }
        fat.setTable(table);
        fat.writeBack();
    }

    /**
     * @desc 将字节数组包装成磁盘块
     * @Date 2019/11/12
     * @param bytes
     * @param off
     * @param end
     * @param num
     * @return file.domain.DiskChunk
     **/
    public DiskChunk wrapToDiskChunk(byte[] bytes, int off, int end, int num) {
        byte[] content = new byte[DISK_CHUNK_SIZE];
        for(int i = 0, j = off; j < end; i++, j++) {
            content[i] = bytes[j];
        }
        return new DiskChunk(num, content);
    }
}
