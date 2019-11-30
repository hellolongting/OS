package equipment_process_storage.memory;

import equipment_process_storage.common.AreaState;

/**
 * @author superferryman
 * @desc 内存分区
 * @date 2019/11/24 15:15
 */
public class MemoryBlock<T> {
    /**
     * 分区号
     */
    private int id;

    /**
     * 分区大小
     */
    private int size;

    /**
     * 分区起始地址
     */
    private int begin;

    /**
     * 分区状态
     */
    private AreaState state;

    /**
     * 存放的数据
     */
    private T data;

    /**
     * 进程标识符
     */
    private Long processId;

    public MemoryBlock() {
    }

    public MemoryBlock(int id, int size, int begin, AreaState state) {
        this.id = id;
        this.size = size;
        this.begin = begin;
        this.state = state;
    }

    public MemoryBlock(int id, int size, AreaState state, Long processId) {
        this.id = id;
        this.size = size;
        this.state = state;
        this.processId = processId;
    }

    public MemoryBlock(int id, int size, AreaState state, T data, Long processId) {
        this.id = id;
        this.size = size;
        this.state = state;
        this.data = data;
        this.processId = processId;
    }

    public MemoryBlock(int id, int size, int begin, AreaState state, Long processId) {
        this.id = id;
        this.size = size;
        this.begin = begin;
        this.state = state;
        this.processId = processId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public AreaState getState() {
        return state;
    }

    public void setState(AreaState state) {
        this.state = state;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
