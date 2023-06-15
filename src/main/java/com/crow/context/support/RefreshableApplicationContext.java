package com.crow.context.support;

import com.crow.context.ApplicationListener;

public class RefreshableApplicationContext extends AbstractRunnableApplicationContext  {

    public void refresh(){
        //初始化线程工厂
        initThreadExecutor();
        //初始化进程消费者
        initProcessConsumer();
        //初始化监听器
        initApplicationEventMulticaster();
    }

    public void addListener(ApplicationListener listener){
        registerListeners(listener);
    }





}
