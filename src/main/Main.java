package main;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.stage.StageStyle.UNDECORATED;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setMaximized(true);
        primaryStage.initStyle(UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /**
        Button login = new Button("Login");
        login.setLayoutX(420);
        login.setLayoutY(380);
        //取消边框
        login.setBorder(null);
        //按钮背景
        login.setBackground(new Background(new BackgroundFill(Paint.valueOf("#8A8A8A"), new CornerRadii(5), Insets.EMPTY)));
        login.setTextFill(Color.BLUE);
        login.setFont(Font.font("sans-serif",25));
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().addAll(getRectangle(),login);


        为“login”按钮设置监听事件，点击了就进入主界面

        login.setOnAction(e -> {
            primaryStage.close();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        });

        primaryStage.setScene(new Scene(anchorPane,1000,575));
        primaryStage.show();
        */
    }

    /**
     * 获得登录界面上的动画
     */
    public Rectangle getRectangle() {
        Rectangle rectParallel = new Rectangle(200,200,50, 50);
        rectParallel.setArcHeight(15);
        rectParallel.setArcWidth(15);
        rectParallel.setFill(Color.DARKBLUE);
        rectParallel.setTranslateX(50);
        rectParallel.setTranslateY(75);

        //定义矩形的淡入淡出效果
        FadeTransition fadeTransition=new FadeTransition(Duration.millis(3000), rectParallel);
        fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.3f);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);
        //fadeTransition.play();

        //定义矩形的平移效果
        TranslateTransition translateTransition=new TranslateTransition(Duration.millis(2000), rectParallel);
        translateTransition.setFromX(50);
        translateTransition.setToX(350);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);
        //translateTransition.play();

        //定义矩形旋转效果
        RotateTransition rotateTransition =
                new RotateTransition(Duration.millis(2000), rectParallel);
        //旋转度数
        rotateTransition.setByAngle(180f);
        rotateTransition.setCycleCount(4);
        rotateTransition.setAutoReverse(true);

        //矩形的缩放效果
        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(2000), rectParallel);
        scaleTransition.setToX(2f);
        scaleTransition.setToY(2f);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        //scaleTransition.play();

        //并行执行动画
        ParallelTransition parallelTransition=new ParallelTransition(fadeTransition,rotateTransition,
                translateTransition,scaleTransition);
        parallelTransition.setCycleCount(Timeline.INDEFINITE);
        parallelTransition.play();

        return rectParallel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
