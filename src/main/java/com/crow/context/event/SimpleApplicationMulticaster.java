package com.crow.context.event;

import com.crow.context.ApplicationEvent;
import com.crow.context.ApplicationException;
import com.crow.context.ApplicationListener;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SimpleApplicationMulticaster implements ApplicationEventMulticaster {

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        applicationListeners.add(listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
            if(supportsEvent(applicationListener,event)){
                applicationListener.onApplicationEvent(event);
            }
        }
    }

    private boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event){
        Type type = applicationListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ApplicationException("wrong event class name: " + className);
        }
        return eventClassName.isAssignableFrom(event.getClass());
    }
}
