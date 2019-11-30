package file.domain;

import file.driver.DiskDriver;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName TextFile
 * @Description 文本文件
 * @Date 2019/11/12
 **/
public class TextFile extends AbstractFile {
    private String text = null;

    TextFile() {}

    public TextFile(String name, FileType type, boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory) {
        super(name, type, isReadOnly, isSystemFile, isWritable, isDirectory);
    }

    /**
     * @desc 获取文本内容
     * @Date 2019/11/17
     * @param
     * @return java.lang.String
     **/
    public String getText() {
        if(text == null) {
            List<DiskChunk> list = read();
            byte[] bytes = new byte[getLength()];
            int size = DiskDriver.DISK_CHUNK_SIZE;
            for(int i = 0; i < list.size(); i++) {
                for(int j = 0; j < size; j++) {
                    if(i * size + j < getLength()) {
                        bytes[i * size + j] = list.get(i).getContent()[j];
                    } else {
                        break;
                    }
                }
            }
            text = new String(bytes, 0, getLength());
        }
        return text;
    }

    /**
     * @desc 保存文本
     * @Date 2019/11/17
     * @param
     * @return void
     **/
    public void saveText() {
        int num = getNumber();
        int[] table = FileBuffer.fat.getTable();
        //释放原空间
        do {
            int temp = num;
            num = table[num];
            table[temp] = 0;
        } while(num != 1);
        num = getNumber();
        int size = DiskDriver.DISK_CHUNK_SIZE;
        byte[] bytes = text.getBytes();
        //重新写入
        for(int i = 0; i < bytes.length; i += size) {
            int end = i + size > bytes.length ? bytes.length : i + size;
            getDiskDriver().write(getDiskDriver().wrapToDiskChunk(bytes, i, end, num));
            table[num] = 1;
            if(i + size < bytes.length) {
                int last = num;
                num = FileBuffer.fat.getUnusedChunk();
                table[last] = num;
            }
        }
        setLength(bytes.length);
        updateParent();
    }

    public void setText(String text) {
        this.text = text;
    }
}
