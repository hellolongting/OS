package process.thread;

import file.domain.ExecutableFile;
import process.action.ProcessAction;
import process.action.SchedulingAction;

import java.util.Iterator;
import java.util.List;

/**
 * @author lenovo
 */
public class CreateProcessesThread extends Thread{

    private List<ExecutableFile> executableFiles;
    private ExecutableFile executableFile;

    public CreateProcessesThread(ExecutableFile executableFile) {
        this.executableFile = executableFile;
        this.executableFiles = SchedulingAction.executableFiles;
    }

    @Override
    public void run() {
        try {
            Iterator var1 = this.executableFiles.iterator();

            while(var1.hasNext()) {
                ExecutableFile file = (ExecutableFile)var1.next();
                /**
                 * 模拟随机创建所有的可执行文件为进程
                 */
                Thread.sleep((long)(0 + (int)(Math.random() * 1000.0D)));
                if (!file.equals(this.executableFile)) {
                    System.out.println(file.toString());
                    ProcessAction.create(file);
                }
            }
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

}
