package equipment_process_storage.memory;

import equipment_process_storage.common.AreaState;
import equipment_process_storage.common.Node;

/**
 * @author superferryman
 * @desc 用户区
 * @date 2019/11/24 16:04
 */
public class UserArea {
    private static UserArea instance = null;

    private Node<MemoryBlock> headNode;
    private Node<MemoryBlock> tailNode;

    public static UserArea getInstance() {
        if (instance == null) {
            instance = new UserArea();
        }
        return instance;
    }

    private UserArea() {
        MemoryBlock block = new MemoryBlock(0, 2048, 0, AreaState.FREE);
        tailNode = new Node<>(block, null, null);
        headNode = new Node<>(null, null, null);
        tailNode.prior = headNode;
        headNode.next = tailNode;
    }

    public Node<MemoryBlock> getHeadNode() {
        return headNode;
    }

    public Node<MemoryBlock> getTailNode() { return tailNode; }

    public int getMaxSize() {
        return 2048;
    }
}
