package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.context.ApplicationListener;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

public class ScheduleNoticeRenderEventListener implements ApplicationListener<ScheduleNoticeRenderEvent> {
    private StringProperty runningNotice;

    public ScheduleNoticeRenderEventListener(StringProperty runningNotice) {
        this.runningNotice = runningNotice;
    }

    @Override
    public void onApplicationEvent(ScheduleNoticeRenderEvent event) {
        Platform.runLater(()-> runningNotice.setValue("PID为"+event.getCurrentPCB().getPid()+"的进程正在运行... 当前"
                +(event.getCurrentPCB().getLife().equals(1)?"是":"不是")
                +"最后一个生命周期"));
    }
}
