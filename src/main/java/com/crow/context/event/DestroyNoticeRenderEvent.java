package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.entity.ProcessPCB;
import lombok.Getter;

@Getter
public class DestroyNoticeRenderEvent extends ApplicationEvent {
    private ProcessPCB PCB;
    public DestroyNoticeRenderEvent(Object source, ProcessPCB PCB) {
        super(source);
        this.PCB = PCB;
    }
}
