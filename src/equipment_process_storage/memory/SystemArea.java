package equipment_process_storage.memory;

import equipment_process_storage.common.AreaState;
import equipment_process_storage.common.Node;
import process.domain.pcb.PCB;
import process.domain.pcb.PCBNode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author superferryman
 * @desc 系统区
 * @date 2019/11/24 15:52
 */
public class SystemArea {
    private static SystemArea instance = null;

    private PCBNode[] pcbArea;
    private AtomicInteger sizeOfPcbArea;
    private Node<MemoryBlock> headNode;

    public static SystemArea getInstance() {
        if (instance == null) {
            instance = new SystemArea();
        }
        return instance;
    }

    private SystemArea() {
        pcbArea  = new PCBNode[10];
        MemoryBlock block = new MemoryBlock(0, 20480, 0, AreaState.FREE);
        headNode = new Node<>(block, null, null);
        sizeOfPcbArea = new AtomicInteger(0);
    }

    public PCBNode[] getPcbArea() {
        return pcbArea;
    }

    public Node<MemoryBlock> getHeadNode() {
        return headNode;
    }

    public int getSizeOfPcbArea() {
        return sizeOfPcbArea.get();
    }

    public void setSizeOfPcbArea(int size) {
        sizeOfPcbArea.set(size);
    }
}
