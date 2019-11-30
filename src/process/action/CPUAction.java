package process.action;

import equipment.domain.EquipmentEnum;
import equipment.driver.EquipmentDriver;
import equipment_process_storage.util.MemoryScheduler;
import process.ProcessController;
import process.domain.MyProcess;
import process.domain.ProcessQueue;
import process.domain.pcb.PCB;
import process.thread.CPUThread;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author lenovo
 */
public class CPUAction {
    /**
     * 程序状态
     */
    private int psw;
    /**
     * 当前运行进程阻塞原因，仅阻塞有值
     */
    private int reason;
    /**
     * 当前指令IR
     */
    private String IR;
    /**
     * 正在运行程序的内存起始位置
     */
    private int start;
    /**
     * \程序计数器
     */
    private int PC;
    /**
     * 数据寄存器
     */
    private int AX;

    /**
     * 当前正在执行的进程
     */
    private MyProcess p;

    /**
     * 用来记录空闲进程连续调用的次数，一次来确定模拟的暂停
     */
    private int count = 0;

    public CPUAction() {
        psw = PCB.Ready;
        reason = 0;
        start = 0;
        PC = 0;
        AX = 0;
        p = null;
    }

    /**
     * CPU()函数
     */
    public void cpu() {
        initProcess();
        /**
         * 模拟CPU不间断工作
         */
        while(CPUThread.flag){
            // 先判断是否发生中断,先处理中断
            if(psw > PCB.Ready){
                p.getPcb().setPC(PC);
                p.getPcb().setAX(AX);
                // 释放数据寄存器的数据
                AX = 0;
                ProcessAction.processController.setIntermediateResult("");

                if(psw == PCB.timeOut){
                    ProcessAction.readyProcessQueue.add(p);
                    p = ProcessAction.readyProcessQueue.poll();
                    ProcessAction.readyProcessRefresh();
                }else if(psw == PCB.IOinterrupt || psw == PCB.Runinterrupt) {
                    p.getPcb().setPSW(psw);
                    p.setReason(reason);
                    // 阻塞 p
                    ProcessAction.block(p);
                    if( !ProcessAction.readyProcessQueue.isEmpty()){
                        p = ProcessAction.readyProcessQueue.poll();
                        ProcessAction.readyProcessRefresh();
                    }else {
                        p = ProcessAction.getNullProcess();
                    }

                }else if(psw == PCB.ENDinterrupt){
                    if(!p.equals(ProcessAction.getNullProcess())){
                        ProcessAction.destroy(p);
                    }

                    if( !ProcessAction.readyProcessQueue.isEmpty()){
                        p = ProcessAction.readyProcessQueue.poll();
                        ProcessAction.readyProcessRefresh();
                    }else {
                        /**
                         * 判断此时是否是连续的调用“空闲进程”,
                         * count > 3 表示连续调用3次，实际上只要3次（秒）就能无确定是否有新进程生成，因为进程的每隔1秒-2秒生成一个
                         */
//                        if(p.equals(ProcessAction.getNullProcess()) && ProcessAction.blockProcessQueue.isEmpty() && (++count) > 3){
//                            check();
//                        }
                        p = ProcessAction.getNullProcess();
                    }

                }
                if(p.equals(ProcessAction.getNullProcess())){
                    PC = 0;
                    ProcessAction.processController.setRunningId("空闲进程");
                }else {
                    PC = p.getPcb().getPC();
                    ProcessAction.processController.setRunningId(p.getName()+"("+String.valueOf(p.getId())+")");
                    count = 0;
                }
                start = p.getPcb().getStart();
                AX = p.getPcb().getAX();
                psw = PCB.Ready;
                p.getPcb().setTimeCell(MyProcess.TIMESLICE);

            }else {
                ProcessAction.processController.setIntermediateResult("AX = "+AX);
            }
            // 从内存中获取指令
            // 获取方式变更:只需要传入当初申请这个空间存放可执行文件的进程id和当前执行到第几条指令即可获取对应的指令
            // 需要捕获异常
            try {
                IR = MemoryScheduler.getInstructionsFromUserArea(p.getPcb().getID(), PC);
                System.out.println("IR : "+IR);
            } catch (Exception e) {
                // 可自行添加恢复逻辑
                System.out.println("PC不正确。");
                continue;
//                check();
            }
            ProcessAction.processController.setExecutionTime(String.valueOf(p.getPcb().getTime()));
            this.explain(IR);
            PC++;
            p.getPcb().decrease();
            if(p.getPcb().isTimeOut() && psw == PCB.Ready){
                psw = PCB.timeOut;
            }

            //模拟一秒一条指令，相对时钟
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            EquipmentDriver.run();
            // 将已经完成设备调用的进程唤醒
            ProcessAction.checkAll();
        }
    }

    /**
     * 刚进来时，要给CPU一个进程
     */
    private void initProcess() {
        if( !ProcessAction.readyProcessQueue.isEmpty()){
            p = ProcessAction.readyProcessQueue.poll();
        }else {
            p = ProcessAction.getNullProcess();
            PC=0;
        }
        start = p.getPcb().getStart();
        PC = p.getPcb().getPC();
        AX = p.getPcb().getAX();
        psw = PCB.Ready;
        p.getPcb().setTimeCell(MyProcess.TIMESLICE);
        ProcessAction.processController.setRunningId(p.getName()+"("+String.valueOf(p.getId())+")");
    }

    /**
     *  解析每一条指令
     * @param program
     */
    public void explain(String program){
        String s1="[xX]=[0-9]";
        String s2="[xX][+][+]";
        String s3="[xX][-][-]";
        String s4="![A|B|C][0-9]";
        String s5="end";

        // 在界面显示当前指令
        ProcessAction.processController.setCurrentInstruction(program);

        if(Pattern.matches(s1, program)){
            //赋值语句 x=?  给 x 赋值（数值一位数）
            AX = Integer.valueOf(program.substring(2));
        }else if(Pattern.matches(s2, program)){
            //x++  x 加 1 （设 x 值总是小于等于 255、大于等于 0）。
            AX++;
        }else if(Pattern.matches(s3, program)){
            //x--   x 减 1。
            AX--;
        }else if(Pattern.matches(s4, program)){
            //!? ?   !是“特殊命令（I/O）的前缀”，第一个?为 A,B,C 中的某个设备，第二个?为一位整数，表示使用设备的时间
            EquipmentEnum name = (program.substring(1,2).equals("A") ? EquipmentEnum.A : (program.substring(1,2).equals("B") ? EquipmentEnum.B : EquipmentEnum.C));
            int e = EquipmentDriver.allocateEquipment(name, Integer.valueOf(program.substring(2)), p.getId());
            if(e<0){
                System.out.println("设备分配失败！");
                psw = PCB.IOinterrupt;
                switch (program.substring(1,2)){
                    case "A": reason = PCB.lackA; break;
                    case "B": reason = PCB.lackB; break;
                    case "C": reason = PCB.lackC; break;
                }
                //回退，等待设备有空再进行请求
                PC--;
            }else {
                psw = PCB.Runinterrupt;
                switch (program.substring(1,2)){
                    case "A": reason = PCB.runA; break;
                    case "B": reason = PCB.runB; break;
                    case "C": reason = PCB.runC; break;
                }
            }

        }else if(Pattern.matches(s5, program)){
            //end 表示“可执行文件”结束
            psw = PCB.ENDinterrupt;
        }
    }

    /**
     * 判断是否需要关闭进程模拟
     * TODO 我觉得如果没有可执行的进程的话，一直执行空闲进程不是很合理，可以考虑一下在某个地方使用该方法，进行CPU模拟的停止。不过暂时没想好加在哪里，
     */
    public void check(){
        try {
            // 为了防止误判，先休眠2000，再检测是否需要全部可执行文件对应的进程都跑完了
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        if(ProcessAction.isEmpty()){
            System.out.println("所有可执行文件创建的进程都执行完毕！");
//            ProcessController.informationDialog("所有可执行文件创建的进程都执行完毕！","只有空闲进程");
            // 关闭进程模拟
            ProcessAction.processController.endSimulation();
        }
    }

}
