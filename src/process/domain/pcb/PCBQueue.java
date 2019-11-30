package process.domain.pcb;

import equipment_process_storage.memory.SystemArea;
import equipment_process_storage.util.MemoryScheduler;
import process.action.ProcessAction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
public class PCBQueue {
    /**
     * 只允许十个PCB
     */
    private static PCBNode[] queue;
    /**
     * 空闲PCB
     */
    private static PCB nullPCB;
    /**
     * 已分配PCB索引
     */
    private static int top;
    private static int tail;
    /**
     * 空白PCB索引
     */
    private static int blankIndex;

    static {
        /**
         * 直接从内存取这长度为10 的数组
         */
        queue = SystemArea.getInstance().getPcbArea();
        top = -1;
        tail = -1;
        blankIndex = 0;
        for(int i=0;i<9;i++){
            queue[i] = new PCBNode();
            queue[i].next = i+1;
        }
        queue[9] = new PCBNode();
//        queue[9].next = -1;
        /**
         * 获得nullPCB ，并赋值给空闲进程
         */
        long processId = ProcessAction.getNullProcess().getId();
        List<String> array = new ArrayList<>();
        array.add("end");
        // 已更改调用逻辑
        int start = MemoryScheduler.allocUserAreaMemory(0, "end;".getBytes().length, array, processId);
        nullPCB = new PCB(processId, start);
        ProcessAction.getNullProcess().setPcb(nullPCB);
    }

    public static boolean isEmpty(){
        if((top == -1 && tail == -1) || (top == tail && top == blankIndex)){
            return true;
        }
        return false;
    }

    /**
     * 进程请求PCB
     */
    public static int apply(){
        int index = blankIndex;
        if(index != -1){
            blankIndex = queue[blankIndex].next;
        }
        return index;
    }

    /**
     * 归还没有创建成功进程的已apply 申请的下标
     * @param index
     */
    public static void giveBackIndex(int index){
        queue[index].next = blankIndex;
        blankIndex = index;
    }

    public static void put(PCB p, int index){
        //当已分配队列为空时
        if(PCBQueue.isEmpty()){
            top = tail = index;
        }else {
            queue[tail].next = index;
            tail = index;
        }
        queue[tail].next = -1;
        queue[tail].pcb = p;
    }

    /**
     * 回收PCB
     */
    public static void free(PCB pcb){
        int i,j,index=top;
        if(top == tail){
            //只有一个PCB，直接删除,进行整体下标恢复
            initPCB();
            return;
        }
        i=j=top;
        while(i <= tail){
            if(queue[top] != null && queue[i].pcb.getID().equals(pcb.getID())){
                if(i == j){
                    top = queue[top].next;
                }else{
                    index = i;
                    queue[j].next = queue[i].next;
                }
                break;
            }
            j=i++;
        }
        queue[index].next = blankIndex;
        blankIndex = index;
    }

    /**
     * 在开始CPU模拟,或者PCB为空的时候，要进行PCB数组的下标恢复
     */
    public static void initPCB(){
        top = -1;
        tail = -1;
        blankIndex = 0;
        for(int i=0;i<9;i++){
            queue[i].next = i+1;
        }
        queue[9].next = -1;
    }

}
