package equipment_process_storage.util;

import equipment_process_storage.EPSController;
import equipment_process_storage.common.AreaState;
import equipment_process_storage.common.Node;
import equipment_process_storage.memory.MemoryBlock;
import equipment_process_storage.memory.SystemArea;
import equipment_process_storage.memory.UserArea;
import process.domain.pcb.PCBNode;

import java.util.List;

/**
 * @author superferryman
 * @desc 内存调度管理器
 * @date 2019/11/24 15:44
 */
public class MemoryScheduler {

    private static EPSController controller;

    public static void initController(EPSController c) { controller = c; }

    /**
     * 为可执行文件分配内存空间
     * @param id 分区id(最好不重复，仅为了展示美观)
     * @param size 文件占用大小
     * @param processId 进程id
     * @return 分配内存空间的首地址 -1表示分配失败
     */
    public static int allocUserAreaMemory(int id, int size, List<String> data, Long processId) {
        MemoryBlock<List<String>> area = new MemoryBlock<>(id, size, AreaState.USED, data, processId);
        int result = firstFit(area, UserArea.getInstance().getHeadNode());
        if (result > 0) {
            UserArea.getInstance().setUsed(UserArea.getInstance().getUsed() + size);
            controller.updateChart();
        }
        return result;
    }

    /**
     * 为主存调用分配内存空间
     * @param id 分区id(最好不重复，仅为了展示美观)
     * @param size 文件占用大小
     * @param processId 进程id
     * @return 是否分配成功
     */
    public static <T> boolean allocSystemAreaMemory(int id, int size, T data, Long processId) {
        MemoryBlock<T> area = new MemoryBlock<>(id, size, AreaState.USED, data, processId);
        return firstFit(area, SystemArea.getInstance().getHeadNode()) != -1;
    }

    /**
     * 为PCB分配内存空间
     * @param pcb 待添加到内存的pcb
     * @return 返回是否分配成功
     */
    public static boolean allocMemoryForPCB(PCBNode pcb) {
        PCBNode[] pcbArea = SystemArea.getInstance().getPcbArea();
        int currentSize = SystemArea.getInstance().getSizeOfPcbArea();
        if (currentSize >= 10) {
            return false;
        } else {
            for (int i = 0; i < pcbArea.length; i ++) {
                if (pcbArea[i] == null) {
                    pcbArea[i] = pcb;
                    break;
                }
            }
            SystemArea.getInstance().setSizeOfPcbArea(currentSize + 1);
            return true;
        }
    }

    /**
     * 释放PCB占用的内存空间
     * @param pcbId 待释放的pcb的Id
     */
    public static void freeMemoryForPCB(Long pcbId) {
        PCBNode[] pcbArea = SystemArea.getInstance().getPcbArea();
        int currentSize = SystemArea.getInstance().getSizeOfPcbArea();
        if (currentSize > 0) {
            for (int i = 0; i < pcbArea.length; i ++) {
                if (pcbArea[i].getPcb().getID().equals(pcbId)) {
                    pcbArea[i] = null;
                    SystemArea.getInstance().setSizeOfPcbArea(currentSize - 1);
                    break;
                }
            }
        }
    }

    /**
     * 通过进程id释放系统进程所占用的内存空间
     * @param processId 进程id
     */
    public static void freeSystemAreaMemory(Long processId) {
        free(processId, SystemArea.getInstance().getHeadNode());
    }

    /**
     * 通过进程id释放用户区可执行文件占用的内存空间
     * @param processId 进程id
     */
    public static void freeUserAreaMemory(Long processId) {
        free(processId, UserArea.getInstance().getHeadNode());
    }

    /**
     * 从分配的用户区内存中获取可执行文件的指令
     * @param processId 获取指令的进程id
     * @param pc 当前执行到的指令数
     * @return 对应的指令 null为超出指令范围 , 因为getByte[] 有问题，所以不用改返回值为String
     * @throws Exception 异常交给上层处理，错误最大可能为数组越界
     */
    public static String getInstructionsFromUserArea(Long processId, int pc) throws Exception {
        Node<MemoryBlock> p = UserArea.getInstance().getHeadNode().next;
        List<String> instructions = null;
        while (true) {
            if (p.data.getProcessId() != null && p.data.getProcessId().equals(processId)) {
                instructions = (List<String>) p.data.getData();
                break;
            }
            p = p.next;
        }
        if (instructions != null) {
            return instructions.get(pc);
        }
        return null;
    }

    /**
     * 内存释放算法
     * @param processId 进程ID
     * @param head 需要释放内存的列表
     */
    private static void free(Long processId, Node<MemoryBlock> head) {
        Node<MemoryBlock> p = head.next;
        while (p != null) {
            if (p.data.getState().getCode() == AreaState.USED.getCode() && p.data.getProcessId().equals(processId)) {
                p.data.setState(AreaState.FREE);
                p.data.setProcessId(null);
                /*
                * 1. 前一个内存块空闲
                * 2. 下一个内存块空闲
                * 3. 前后内存块均空闲
                */
                if (p.prior == head) {
                    if (p.next.data.getState().getCode() == AreaState.FREE.getCode()) {
                        p.data.setSize(p.data.getSize() + p.next.data.getSize());
                        if (p.next.next != null) {
                            p.next.next.prior = p;
                        }
                        p.next = p.next.next;
                        UserArea.getInstance().setUsed(UserArea.getInstance().getUsed() - p.data.getSize());
                        controller.updateChart();
                    }
                    break;
                }
                if ((p.prior.data.getState().getCode() == AreaState.FREE.getCode())
                        && (p.next.data.getState().getCode() == AreaState.FREE.getCode())) {
                    p.prior.data.setSize(p.data.getSize() + p.prior.data.getSize() + p.next.data.getSize());
                    p.prior.next = p.next.next;
                    if (p.next.next != null && p.next.next.prior != null) {
                        p.next.next.prior = p.prior;
                    }
                    UserArea.getInstance().setUsed(UserArea.getInstance().getUsed() - p.data.getSize());
                    controller.updateChart();
                } else if (p.prior.data.getState().getCode() == AreaState.FREE.getCode()) {
                    p.prior.data.setSize(p.data.getSize() + p.prior.data.getSize());
                    p.prior.next = p.next;
                    p.next.prior = p.prior;
                    UserArea.getInstance().setUsed(UserArea.getInstance().getUsed() - p.data.getSize());
                    controller.updateChart();
                } else if (p.next.data.getState().getCode() == AreaState.FREE.getCode()) {
                    p.data.setSize(p.data.getSize() + p.next.data.getSize());
                    if (p.next.next != null) {
                        p.next.next.prior = p;
                    }
                    p.next = p.next.next;
                    UserArea.getInstance().setUsed(UserArea.getInstance().getUsed() - p.data.getSize());
                    controller.updateChart();
                }
                break;
            }
            p = p.next;
        }
    }

    /**
     * 首次适配算法
     * @param area 待分配内存的块
     * @param head 需要使用首次适配算法进行匹配的内存表链表的头节点
     * @return 分配空间的首地址 -1表示分配失败
     */
    private static int firstFit(MemoryBlock area, Node<MemoryBlock> head) {
        Node<MemoryBlock> temp = new Node<>();
        temp.data = area;

        Node<MemoryBlock> p = head.next;
        while (p != null) {
            // 恰好合适或有可以放下的空间
            if (p.data.getState().getCode() == AreaState.FREE.getCode() &&
                    p.data.getSize() == area.getSize()) {
                p.data.setState(AreaState.USED);
                p.data.setId(area.getId());
                p.data.setProcessId(area.getProcessId());
                return p.data.getBegin();
            } else if (p.data.getState().getCode() == AreaState.FREE.getCode() &&
                    p.data.getSize() >= area.getSize()) {
                temp.prior = p.prior;
                temp.next = p;
                temp.data.setBegin(p.data.getBegin());
                p.prior.next = temp;
                p.prior = temp;
                p.data.setBegin(temp.data.getBegin() + temp.data.getSize());
                p.data.setSize(p.data.getSize() - temp.data.getSize());
                return temp.data.getBegin();
            }
            p = p.next;
        }
        return -1;
    }
}
