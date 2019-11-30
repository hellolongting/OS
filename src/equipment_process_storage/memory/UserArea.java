package equipment_process_storage.memory;

import equipment_process_storage.common.AreaState;
import equipment_process_storage.common.Node;
import javafx.scene.chart.PieChart;

/**
 * @author superferryman
 * @desc 用户区
 * @date 2019/11/24 16:04
 */
public class UserArea {
    private static UserArea instance = null;

    private Node<MemoryBlock> headNode;
    private Node<MemoryBlock> tailNode;
    private double used;

    public static UserArea getInstance() {
        if (instance == null) {
            instance = new UserArea();
        }
        return instance;
    }

    private UserArea() {
        used = 0;
        MemoryBlock block = new MemoryBlock(0, 512, 0, AreaState.FREE);
        tailNode = new Node<>(block, null, null);
        headNode = new Node<>(null, null, null);
        tailNode.prior = headNode;
        headNode.next = tailNode;
    }

    public Node<MemoryBlock> getHeadNode() {
        return headNode;
    }

    public Node<MemoryBlock> getTailNode() { return tailNode; }

    public double getUsed() {
        return used;
    }

    public void setUsed(double used) {
        this.used = used;
    }

    public double getMaxSize() {
        return 512.0;
    }
}
