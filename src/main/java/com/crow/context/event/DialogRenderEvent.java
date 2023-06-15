package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.entity.ProcessPCB;
import lombok.Getter;

import java.util.List;
@Getter
public class DialogRenderEvent extends ApplicationEvent {

    private List<ProcessPCB> pcbList;



    public DialogRenderEvent(Object source, List<ProcessPCB> pcbList) {
        super(source);
        this.pcbList = pcbList;

    }
}
