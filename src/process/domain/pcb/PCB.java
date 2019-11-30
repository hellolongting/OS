package process.domain.pcb;

/**
 * @author lenovo
 */
public class PCB {

    /**
     * 直接设定为进程的ID
     */
    private Long id;
    private int start;
    private int PSW;
    private int timeCell;
    private int PC;
    private int AX;
    /**
     * 阻塞原因
     */
    private int reason;

    public static final int  Ready=0;
    /**
     * IO中断
     */
    public static final int  IOinterrupt=1;
    /**
     * 软中断
     */
    public static final int  ENDinterrupt=2;
    /**
     * 设备运行中断
     */
    public static final int  Runinterrupt=3;
    /**
     * 时间片结束
     */
    public static final int  timeOut=4;
    /**
     *  缺少设备A
     */
    public static final int lackA=1;
    /**
     * 缺少设备B
     */
    public static final int lackB=2;
    /**
     * 缺少设备C
     */
    public static final int lackC=3;

    public static final int runA=4;
    public static final int runB=5;
    public static final int runC=6;

    public PCB(Long id,int start)
    {
        this.id=id;
        this.start=start;
        PSW=Ready;
        reason=-1;
        timeCell=0;
        PC=0;
        AX=0;
    }

    public Long getID()
    {
        return id;
    }

    public int getStart()
    {
        return start;
    }

    public void setPSW(int psw)
    {
        PSW=psw;
    }

    public int getPSW()
    {
        return PSW;
    }

    public void setTimeCell(int time)
    {
        timeCell=time;
    }

    public void decrease()
    {
        timeCell--;
    }

    public int getTime()
    {
        return timeCell;
    }

    public boolean isTimeOut()
    {
        if( timeCell==0 )
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void setPC(int pc)
    {
        PC=pc;
    }

    public int getPC()
    {
        return PC;
    }

    public void setReason(int r)
    {
        this.reason=r;
    }

    public int getReason()
    {
        return reason;
    }

    public int getAX() {
        return AX;
    }

    public void setAX(int AX) {
        this.AX = AX;
    }
}
