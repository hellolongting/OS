package util;

import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Controller;

/**
 * @author lenovo
 */
public class SingletonStageUtil {
    public static Controller controller;

    /**
     * 文件窗口
     */
    public static Stage fileStage;
    /**
     * EPS窗口
     */
    public static Stage epsStage;

    /**
     * 单例获取文件窗口
     */
    public static Stage getFileStage(){
        if(fileStage == null){
            fileStage = new Stage();
        }
//        thumbnail_file.setStyle("-fx-background-color: linear-gradient(#4F4F4F 90%, rgb(215, 239, 233) 90%);");
        return fileStage;
    }
    /**
     * 单例获取EPS窗口
     * @param controller
     * @param thumbnail_eps
     */
    public static Stage getEpsStage(Controller controller){
        SingletonStageUtil.controller = controller;
        if(epsStage == null){
            epsStage = new Stage();
        }
//        thumbnail_eps.setStyle("-fx-background-color: linear-gradient(#4F4F4F 90%, rgb(215, 239, 233) 90%);");
        return epsStage;
    }


}
