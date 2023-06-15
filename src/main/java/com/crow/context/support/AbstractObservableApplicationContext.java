package com.crow.context.support;

import com.crow.context.ApplicationContext;
import com.crow.context.ApplicationEvent;
import com.crow.context.ApplicationListener;
import com.crow.context.event.ApplicationEventMulticaster;
import com.crow.context.event.DialogRenderEventListener;
import com.crow.context.event.SimpleApplicationMulticaster;

public abstract class AbstractObservableApplicationContext implements ApplicationContext {

    private ApplicationEventMulticaster applicationEventMulticaster;


    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    protected void initApplicationEventMulticaster(){
        applicationEventMulticaster = new SimpleApplicationMulticaster();
    }

    protected void registerListeners(ApplicationListener listener){
        applicationEventMulticaster.addApplicationListener(listener);
    }
}
