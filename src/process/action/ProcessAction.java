package process.action;

import equipment.driver.EquipmentDriver;
import equipment_process_storage.util.MemoryScheduler;
import file.domain.ExecutableFile;
import javafx.scene.control.Alert;
import process.ProcessController;
import process.domain.MyProcess;
import process.domain.pcb.PCB;
import process.domain.ProcessQueue;
import process.domain.pcb.PCBQueue;

/**
 * @author lenovo
 */
public class ProcessAction {

    public static int baseId = (int)(Math.random() * 1000) + 10;
    public static ProcessController processController;
    public static ProcessQueue readyProcessQueue;
    public static ProcessQueue blockProcessQueue;
    private static MyProcess nullProcess;

    static {
        readyProcessQueue = new ProcessQueue();
        blockProcessQueue = new ProcessQueue();
        nullProcess = new MyProcess("空闲进程");
    }

    /**
     * 进程创建
     * @param executableFile
     */
    public static boolean create(ExecutableFile executableFile) {
        // 申请进程控制块PCB
        int index = PCBQueue.apply();
        if(index != -1){
            MyProcess process = new MyProcess(executableFile.getName());
            // 申请主存空间
            // 更改调用顺序
            int start = MemoryScheduler.allocUserAreaMemory(baseId++, executableFile.getLength(), executableFile.getInstructions(), process.getId());
            System.out.println("index : "+index+" , start : "+start);
            if(start != -1){
                PCB pcb = new PCB(process.getId(), start);
                PCBQueue.put(pcb, index);
                process.setPcb(pcb);

                readyProcessQueue.add(process);
                readyProcessRefresh();
            }else {
                System.out.println("出错提示：进程 "+process.getName()+" 内存申请失败!\t内存空间不足");
               // ProcessController.informationDialog("内存空间不足", "内存申请失败");
                /**
                 * 创建进程失败，归还刚申请的PCB下标
                 */
                PCBQueue.giveBackIndex(index);
                return false;
            }
        }else {
            System.out.println("出错提示：文件 "+executableFile.getName()+" 创建进程失败，\t最多只能创建十个进程！");
//            ProcessController.informationDialog("你最多只能创建十个进程", "申请PCB失败");
            return false;
        }
        return true;
    }

    /**
     * 进程销毁
     * @param process
     */
    public static void destroy(MyProcess process) {
        // 空闲进程是不能被销毁的
        if(!nullProcess.equals(process)){
            // 回收所申请的主存空间
            MemoryScheduler.freeUserAreaMemory(process.getId());
            // 回收PCB
            PCBQueue.free(process.getPcb());
        }
    }

    /**
     * 进程阻塞
     * @param process
     */
    public static void block(MyProcess process) {
        if(!nullProcess.equals(process)) {
            blockProcessQueue.add(process);
        }
//        readyProcessRefresh();
        blockProcessRefresh();
    }

    /**
     * 唤醒进程
     * @param process
     */
    public static void awake(MyProcess process) {
        if(blockProcessQueue.delete(process) && readyProcessQueue.add(process)){
            System.out.println(process.getName()+" 进程唤醒成功！");
        }else {
            System.out.println("进程唤醒失败！");
        }
        readyProcessRefresh();
        blockProcessRefresh();
    }

    public static MyProcess getNullProcess() {
        return nullProcess;
    }

    public static void clear() {
        readyProcessQueue.clear();
        blockProcessQueue.clear();
    }

    public static boolean isEmpty(){
        return readyProcessQueue.isEmpty() && blockProcessQueue.isEmpty();
    }

    public static void readyProcessRefresh(){
        processController.readyTable.refresh();
    }

    public static void blockProcessRefresh(){
        processController.blockTable.refresh();
    }
    /**
     * 将已经完成设备调用的进程唤醒
     */
    public static void checkAll(){
        int i=0;
        MyProcess p;
        while(i<blockProcessQueue.size() && blockProcessQueue.size() != 0){
            p = blockProcessQueue.get(i);
            if(EquipmentDriver.judgeEquipmentById(p.getId())){
                ProcessAction.awake(p);
                continue;
            }
            i++;
        }
    }

    /**
     * 销毁所有的进程
     */
    public static void destroyAllProcess(){
        while(readyProcessQueue.size() > 0){
            destroy(readyProcessQueue.get(0));
            readyProcessQueue.poll();
        }
        while(blockProcessQueue.size() > 0){
            destroy(blockProcessQueue.get(0));
            blockProcessQueue.poll();
        }
    }

}
