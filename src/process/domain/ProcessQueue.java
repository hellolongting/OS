package process.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 */
public class ProcessQueue {

    private List<MyProcess> queue;

    public ProcessQueue() {
        queue = new ArrayList();
    }

    /**
     * 增加进程到队尾
     * @param p
     */
    public boolean add(MyProcess p) {
        return this.queue.add(p);
    }

    /**
     * 删除队头的进程
     * @return
     */
    public MyProcess poll() {
        return this.queue.remove(0);
    }

    public int size() {
        return this.queue.size();
    }

    public MyProcess get(int i) {
        return this.queue.get(i);
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public void clear() {
        this.queue.clear();
    }

    public List<MyProcess> getQueue() {
        return this.queue;
    }

    public boolean delete(MyProcess process){
        return queue.remove(process);
    }

    @Override
    public String toString() {
        return "ProcessQueue{" +
                "queue=" + queue.toString() +
                '}';
    }
}
