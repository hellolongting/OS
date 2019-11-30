package file.domain;

/**
 * @ClassName DiskChunk
 * @Description 磁盘块
 * @Date 2019/11/11
 **/
public class DiskChunk {
    //磁盘块号
    private int number;
    //内容
    private byte[] content;
    //长度,单位: byte
    private int len;

    public DiskChunk(int number, byte[] content) {
        this.number = number;
        this.content = content;
    }

    //getter & setter
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
