package file.domain;

/**
 * @ClassName FileType
 * @Description 文件类型
 * @Date 2019/11/12
 **/
public enum FileType {
    DIRECTORY(0,"目录"), TEXT_FILE(1,"文本文件"), EXECUTABLE(2,"可执行文件"), ROOT(3, "根目录");
    private String typename;
    private int code;

    FileType(int code, String typename) {
        this.code = code;
        this.typename = typename;
    }

    public static FileType getValue(int code) {
        for(FileType fileType : values()) {
            if(fileType.code == code) {
                return fileType;
            }
        }
        return null;
    }

    public String getTypename() {
        return typename;
    }

    public int getCode() {
        return code;
    }
}
