package com.crow.context;

import com.crow.factory.ProcessOverflowException;

public interface ApplicationContext extends ApplicationEventPublisher{

    void stop();

    void pause();

    void addProcess() throws ProcessOverflowException;

    void resume();
}
