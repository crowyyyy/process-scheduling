package com.crow.context.event;

import com.crow.context.ApplicationListener;
import com.crow.entity.ProcessPCB;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

public class DestroyNoticeRenderEventListener implements ApplicationListener<DestroyNoticeRenderEvent> {
    private StringProperty notice;

    public DestroyNoticeRenderEventListener(StringProperty notice) {
        this.notice = notice;
    }

    @Override
    public void onApplicationEvent(DestroyNoticeRenderEvent event) {
        Platform.runLater(()-> Platform.runLater(()->notice.setValue("PID为"+ event.getPCB().getPid()+"的进程被销毁")));
    }
}
