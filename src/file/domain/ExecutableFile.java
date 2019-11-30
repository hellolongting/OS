package file.domain;

import file.driver.DiskDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ExecutableFile
 * @Description 可执行文件
 * @Date 2019/11/22
 **/
public class ExecutableFile extends AbstractFile{
    //指令集
    private List<String> instructions = null;
    //源指令
    private String code = null;

    ExecutableFile() {}

    public ExecutableFile(String name, FileType type, boolean isReadOnly, boolean isSystemFile, boolean isWritable, boolean isDirectory) {
        super(name, type, isReadOnly, isSystemFile, isWritable, isDirectory);
    }

    /**
     * @desc 获取源指令
     * @param
     * @return java.lang.String
     **/
    public String getCode() {
        if(code == null) {
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
            code = new String(bytes, 0, getLength());
        }
        return code;
    }

    /**
     * @desc 获取指令集
     * @param
     * @return java.util.List<java.lang.String>
     **/
    public List<String> getInstructions() {
        if(code == null) {
            getCode();
        }
        if(instructions == null){
            wrapToList();
        }
        return instructions;
    }

    /**
     * @desc 封装指令集
     * @param
     * @return java.util.List<java.lang.String>
     **/
    private void wrapToList() {
        //封装指令集
        String content = code;
        content.trim();
        instructions = new ArrayList<>();
        for(String instruction : content.split(";")) {
            if(instruction.trim().length() > 0) {
                instructions.add(instruction.trim());
            }
        }
    }

    /**
     * @desc 保存源指令
     * @Date 2019/11/17
     * @param
     * @return void
     **/
    public void saveCode() {
        int num = getNumber();
        int[] table = FileBuffer.fat.getTable();
        //释放原空间
        do {
            int temp = num;
            num = table[num];
            table[temp] = 0;
        } while(num != 1);
        num = getNumber();
        table[num] = 1;
        int size = DiskDriver.DISK_CHUNK_SIZE;
        byte[] bytes = code.getBytes();
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
        //更新指令集
        wrapToList();
    }

    /**
     * @desc 写入源文件
     * @param code
     * @return void
     **/
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
