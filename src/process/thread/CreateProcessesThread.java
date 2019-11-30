package process.thread;

import file.domain.ExecutableFile;
import process.action.ProcessAction;
import process.action.SchedulingAction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author lenovo
 */
public class CreateProcessesThread extends Thread{

    private List<ExecutableFile> executableFiles;
    private ExecutableFile executableFile;

    private List<ExecutableFile> waitingCreateFiles = new ArrayList<>();

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
                Thread.sleep((long)(500 + (int)(Math.random() * 1000.0D)));
                if (!file.equals(this.executableFile)) {
                    System.out.println(file.toString());
                    if(!ProcessAction.create(file)){
                        waitingCreateFiles.add(file);
                    }
                }
            }

            while (!waitingCreateFiles.isEmpty()){
                /**
                 * 先睡眠一定时间，再对没有成功创建进程的可执行文件 再次进行创建
                 */
                Thread.sleep(20000);

                Iterator var2 = this.waitingCreateFiles.iterator();
                int i=0;
                while(i < waitingCreateFiles.size()) {
                    ExecutableFile file = waitingCreateFiles.get(i);
                    Thread.sleep((long)(500 + (int)(Math.random() * 1000.0D)));
                    System.out.println("再次创建 ："+file.toString());
                    if(ProcessAction.create(file)){
                        waitingCreateFiles.remove(i);
                        i--;
                    }
                    i++;
                }
            }

        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

}
