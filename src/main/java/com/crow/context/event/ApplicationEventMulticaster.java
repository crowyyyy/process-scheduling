package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.context.ApplicationListener;

public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener listener);

    void removeApplicationListener(ApplicationListener listener);

    void multicastEvent(ApplicationEvent event);
}
