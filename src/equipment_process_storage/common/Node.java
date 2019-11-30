package equipment_process_storage.common;

/**
 * @author superferryman
 * @desc 链表结点
 * @date 2019/11/24 16:00
 */
public class Node<T> {
    public T data;
    public Node<T> prior;
    public Node<T> next;

    public Node() {
    }

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node<T> prior, Node<T> next) {
        this.data = data;
        this.prior = prior;
        this.next = next;
    }
}
