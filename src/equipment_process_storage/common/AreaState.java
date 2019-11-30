package equipment_process_storage.common;

/**
 * @author superferryman
 * @desc 分区状态
 * @date 2019/11/24 15:19
 */
public enum AreaState {
    /**
     * code 表示状态编码
     * desc 表示状态
     */
    FREE(0, "空闲"),
    USED(1, "占用");

    private final int code;
    private final String desc;

    AreaState(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
