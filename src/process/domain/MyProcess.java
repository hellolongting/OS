package process.domain;

import javafx.beans.property.*;
import process.domain.pcb.PCB;
import util.SingletonStageUtil;

/**
 * @author lenovo
 */
public class MyProcess {
    /**
     * 时间片预设为6
     */
    public static int TIMESLICE = 6;
    public static int baseID = (int)(Math.random() * 100000.0D) + 1;
    private LongProperty id = new SimpleLongProperty();
    private String name;
    private StringProperty reachTime = new SimpleStringProperty();
    private StringProperty reason = new SimpleStringProperty();
    private PCB pcb;

    public MyProcess(PCB pcb) {
        this.setId(baseID++);
        this.setReachTime(SingletonStageUtil.controller.clock.getText());
        this.setPcb(pcb);
    }

    public MyProcess(String name) {
        this.setName(name);
        this.setId(baseID++);
        this.setReachTime(SingletonStageUtil.controller.clock.getText());
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getReachTime() {
        return (String)this.reachTime.get();
    }

    public StringProperty reachTimeProperty() {
        return this.reachTime;
    }

    public void setReachTime(String reachTime) {
        this.reachTime.set(reachTime);
    }

    public PCB getPcb() {
        return this.pcb;
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public String getReason() {
        return this.reason.get();
    }

    public StringProperty reasonProperty() {
        return this.reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReason(int reason) {
        this.pcb.setReason(reason);
        switch (reason){
            case PCB.lackA: this.reason.set("缺少A");break;
            case PCB.lackB: this.reason.set("缺少B");break;
            case PCB.lackC: this.reason.set("缺少C");break;
            case PCB.runA: this.reason.set("调用A");break;
            case PCB.runB: this.reason.set("调用B");break;
            case PCB.runC: this.reason.set("调用C");break;
        }
    }

    @Override
    public String toString() {
        return "MyProcess{" +
                "id=" + id +
                ", reachTime=" + reachTime +
                ", reason=" + reason +
                ", pcb=" + pcb +
                '}';
    }
}
