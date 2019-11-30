package process.action;

import file.domain.ExecutableFile;
import file.domain.FileBuffer;
import process.ProcessController;
import process.domain.pcb.PCBQueue;
import process.thread.CPUThread;
import process.thread.CreateProcessesThread;

import java.util.List;

/**
 * @author lenovo
 */
public class SchedulingAction {

    public static List<ExecutableFile> executableFiles;

    static {
        executableFiles = FileBuffer.exeList;
    }

    /**
     * 开始模拟调度
     * @param processController
     * @param executableFile
     */
    public static void startScheduling(ProcessController processController, ExecutableFile executableFile) {
        PCBQueue.initPCB();
        ProcessAction.clear();
        ProcessAction.processController = processController;
        ProcessAction.create(executableFile);
        /**
         * 利用上面已经创建好的进程，下面开始模拟CPU()
         */
        CPUThread.flag = true;
        CPUThread cpuThread = new CPUThread();
        cpuThread.start();
        /**
         * 创建除了上面文件以外，所有文件的进程（每个文件一个进程）
         */
        CreateProcessesThread createProcessesThread = new CreateProcessesThread(executableFile);
        createProcessesThread.start();
    }

}
