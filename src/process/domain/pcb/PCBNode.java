package process.domain.pcb;

/**
 * @author lenovo
 */
public class PCBNode {

    PCB pcb;
    int next;

    public PCBNode() {
        pcb=null;
        next=-1;
    }

    public PCB getPcb() {
        return pcb;
    }

    public int getNext() {
        return next;
    }
}
