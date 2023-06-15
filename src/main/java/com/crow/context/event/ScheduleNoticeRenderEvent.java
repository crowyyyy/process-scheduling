package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.entity.ProcessPCB;
import lombok.Getter;

import java.util.EventObject;

@Getter
public class ScheduleNoticeRenderEvent extends ApplicationEvent {

    private ProcessPCB currentPCB;

    public ScheduleNoticeRenderEvent(Object source,ProcessPCB currentPCB) {
        super(source);
        this.currentPCB = currentPCB;
    }
}
