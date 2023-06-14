package com.crow.test;

import com.crow.entity.ProcessPCB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Test;

import java.awt.*;

public class JavaFxTest extends Application {
    public volatile Integer i = 5;
    @Test
    public void JavaFxBootTest(){

//        // Create a VBox container to hold the process PCBs
//        VBox vbox = new VBox(10);
//        vbox.setPadding(new Insets(10, 10, 10, 10));
//
//        // Create a Pane to hold the process PCBs
//        Panel root = new Panel();
//
//        // Add the process PCBs to the Pane
//        PanePane.addPcbs(new ProcessPCB(1, 2, 0, 1), new ProcessPCB(2, 3, 1, 2), new ProcessPCB(3, 4, 0, 3));
//
//        // Add the Pane to the VBox
//        vbox.getChildren().add(root);
//
//        // Set the scene and show the stage
//        Scene scene = new Scene(vbox);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    @Test
    public void test(){
        Integer a= 6;
        change(a);
        System.out.println(a);
    }
    public void change(Integer i){
        i = 64;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
