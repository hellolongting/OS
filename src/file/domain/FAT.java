package file.domain;

import file.driver.DiskDriver;
import file.exception.UnavailableChunkException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName FAT
 * @Description 文件分配表
 * @Date 2019/11/11
 **/
public class FAT {
    //占用磁盘块
    private Integer[] number = {0, 1};
    //长度
    private int length = 256;
    //文件分配表
    private int[] table = new int[DiskDriver.DISK_TOTAL];
    //磁盘驱动
    private DiskDriver diskDriver = new DiskDriver();

    public FAT() {
        List<DiskChunk> lists = diskDriver.read(Arrays.asList(number));
        //转化为文件分配表
        for(int i = 0; i < lists.size(); i++) {
            for(int j = 0; j < DiskDriver.DISK_CHUNK_SIZE; j++) {
                table[i * DiskDriver.DISK_CHUNK_SIZE + j] = Byte.toUnsignedInt(lists.get(i).getContent()[j]);
            }
        }
    }

    /**
     * @desc 获取空闲磁盘块
     * @Date 2019/11/12
     * @param
     * @return int
     **/
    public int getUnusedChunk() {
        for(int i = DiskDriver.DISK_BEGIN; i < DiskDriver.DISK_TOTAL; i++) {
            if(table[i] == 0)
                return i;
        }
        new UnavailableChunkException("磁盘空间已满，无空闲磁盘块");
        return -1;
    }

    /**
     * @desc 将fat同步至磁盘
     * @Date 2019/11/12
     * @param
     * @return void
     **/
    public void writeBack() {
        List<DiskChunk> list = new ArrayList<>();
        for(int i = 0; i < number.length; i++) {
            byte[] bytes = new byte[DiskDriver.DISK_CHUNK_SIZE];
            //将int类型转化为字节
            for(int j = 0; j < DiskDriver.DISK_CHUNK_SIZE; j++) {
                bytes[j] = (byte) table[i*DiskDriver.DISK_CHUNK_SIZE + j];
            }
            //构造磁盘块
            list.add(new DiskChunk(number[i], bytes));
        }
        diskDriver.write(list);
    }

    /**
     * @desc 释放某个磁盘块开始的一系列块
     * @Date 2019/11/12
     * @param num
     * @return void
     **/
    public void release(int num) {
        do {
            int temp = num;
            num = table[num];
            table[temp] = 0;
        } while(num != 1);
    }

    public int[] getTable() {
        return table;
    }

    public void setTable(int[] table) {
        this.table = table;
    }
}
