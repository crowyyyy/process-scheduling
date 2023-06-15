package com.crow.gui;

import com.crow.context.event.DestroyNoticeRenderEventListener;
import com.crow.context.event.DialogRenderEventListener;
import com.crow.context.event.ScheduleNoticeRenderEventListener;
import com.crow.context.support.RefreshableApplicationContext;
import com.crow.entity.ProcessPCB;
import com.crow.factory.ProcessOverflowException;
import javafx.application.Application;
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

public class ProcessManagerDialog extends Application  implements ScheduleDialog {
    private final ObservableList<ProcessPCB> DATA = FXCollections.observableArrayList();
    private TableView queueTable = new TableView();
    private ProcessPCB currentPCB;
    private StringProperty runningNoticeContent = new SimpleStringProperty("处理器中没有进程正在运行");
    private StringProperty destroyNoticeContent = new SimpleStringProperty("暂无进程结束信息");
    private RefreshableApplicationContext application = new RefreshableApplicationContext();
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);
    @Override
    public void start(Stage stage){
        application.refresh();
        initComponent(stage);
        stage.show();
    }

    private void initComponent(Stage stage){
        Label notice = new Label();
        Label destroyNotice = new Label();
        stage.setTitle("Process View");
        stage.setWidth(400);
        stage.setHeight(500);
        Button createProcessBtn = new Button("New Process");
        Button pauseBtn = new Button("pause");
        Button resumeBtn = new Button("resume");
        Button stopBtn = new Button("stop");
        createProcessBtn.setOnAction(e->addProcess());
        pauseBtn.setOnAction(e->pause());
        resumeBtn.setOnAction(e->resume());
        stopBtn.setOnAction(e->stop());
        createProcessBtn.setStyle("-fx-background-color: #77DD77;");
        VBox vbox = new VBox(createProcessBtn,pauseBtn,resumeBtn,stopBtn);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
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
        notice.textProperty().bindBidirectional(runningNoticeContent);
        destroyNotice.textProperty().bindBidirectional(destroyNoticeContent);
        vbox.getChildren().addAll(notice,destroyNotice,title,queueTable);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        application.addListener(new DialogRenderEventListener(DATA));
        application.addListener(new ScheduleNoticeRenderEventListener(runningNoticeContent));
        application.addListener(new DestroyNoticeRenderEventListener(destroyNoticeContent));
    }

    @Override
    public void addProcess(){
        try {
            application.addProcess();
        } catch (ProcessOverflowException e) {
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    @Override
    public void pause() {
        application.pause();
    }
    @Override
    public void stop(){
        application.stop();
    }

    @Override
    public void resume() {
        application.resume();
    }

    protected static void run(String[] args){
        launch(args);
    }
}
