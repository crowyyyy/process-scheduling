package com.crow.gui;

import com.crow.context.support.ApplicationContext;
import com.crow.entity.ProcessPCB;
import com.crow.factory.ProcessOverflowException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
public class ProcessManagerDialog extends Application {
    private final ObservableList<ProcessPCB> DATA = FXCollections.observableArrayList();
    private TableView queueTable = new TableView();
    private ProcessPCB currentPCB;
    private StringProperty noticeText = new SimpleStringProperty("处理器中没有进程正在运行");
    private StringProperty destroyNoticeText = new SimpleStringProperty("暂无进程结束信息");
    private ApplicationContext application = new ApplicationContext();
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @Override
    public void start(Stage stage){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue());
        executor.execute(()->{
            while(true){
                System.out.println("线程"+Thread.currentThread().getName()+"正在等待进程");
                run();
            }
        });
        initComponent(stage);
        stage.show();
    }

    private void initComponent(Stage stage){
        // set the message
        Label notice = new Label();
        Label destroyNotice = new Label();
        // Set the parameter of stage
        stage.setTitle("Process View");
        stage.setWidth(400);
        stage.setHeight(500);
        // Create two buttons to switch between different data sets
        Button createProcessBtn = new Button("New Process");
        createProcessBtn.setOnAction(e->createProcess());
        createProcessBtn.setStyle("-fx-background-color: #77DD77;");

        // Add the buttons to the scene
        VBox vbox = new VBox(createProcessBtn);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        // Set the table
        Label title = new Label("队列中的进程信息：");
        title.setFont(new Font("Arial", 20));
        queueTable.setEditable(true);
        TableColumn lifeCol = new TableColumn("Life");
        lifeCol.setCellValueFactory(
                new PropertyValueFactory<ProcessPCB, String>("life"));
        TableColumn priorityCol = new TableColumn("Priority");
        priorityCol.setCellValueFactory(
                new PropertyValueFactory<ProcessPCB, String>("priority"));
        TableColumn pidCol = new TableColumn("PID");
        pidCol.setCellValueFactory(
                new PropertyValueFactory<ProcessPCB, String>("pid"));
        TableColumn statusCol = new TableColumn("status");
        statusCol.setCellValueFactory(
                new PropertyValueFactory<ProcessPCB, String>("status"));
        queueTable.setItems(DATA);
        queueTable.getColumns().addAll(pidCol,lifeCol, priorityCol,statusCol);
        notice.textProperty().bindBidirectional(noticeText);
        destroyNotice.textProperty().bindBidirectional(destroyNoticeText);
        vbox.getChildren().addAll(notice,destroyNotice,title,queueTable);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
    }

    private void createProcess(){
        try {
            application.createProcess();
        } catch (ProcessOverflowException e) {
            alert.setContentText(e.getMessage());
            alert.show();
        }
        refresh();
    }

    private void run(){
        ProcessPCB processPCB = application.fetchProcess();
        if(Objects.isNull(processPCB)){
            return;
        }
        currentPCB = processPCB;
        refresh();
        try {
            System.out.println("process running...");
            System.out.println("当前工作进程："+processPCB);
            Thread.sleep(5000);
            System.out.println("process end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(application.afterRun(processPCB)){
            Platform.runLater(()->destroyNoticeText.setValue("PID为"+processPCB.getPid()+"的进程被销毁"));

        }
    }

    private synchronized void refresh(){
        List<ProcessPCB> pcbList = application.getPriorityQueue();
        DATA.clear();
        DATA.addAll(pcbList);
        Platform.runLater(()-> noticeText.setValue("PID为"+currentPCB.getPid()+"的进程正在运行... 当前"
                +(currentPCB.getLife().equals(1)?"是":"不是")
                +"最后一个生命周期"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
