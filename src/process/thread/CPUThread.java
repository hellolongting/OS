package process.thread;

import process.action.CPUAction;

/**
 * @author longting
 */
public class CPUThread extends Thread{

    /**
     * 控制CPU()是否停止模拟
     */
    public static boolean flag = true;

    @Override
    public void run() {
        try {
            /**
             * 先让第一个线程出现在就绪线程一秒钟
             */
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CPUAction cpu = new CPUAction();
        cpu.cpu();
    }
}
