package com.crow.context.event;

import com.crow.context.ApplicationListener;
import com.crow.entity.ProcessPCB;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import javax.xml.crypto.Data;

public class DialogRenderEventListener  implements ApplicationListener<DialogRenderEvent> {
    private ObservableList<ProcessPCB> dataSource;


    public DialogRenderEventListener(ObservableList<ProcessPCB> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public synchronized void onApplicationEvent(DialogRenderEvent event) {
        dataSource.clear();
        dataSource.addAll(event.getPcbList());

    }
}
