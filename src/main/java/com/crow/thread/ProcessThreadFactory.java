package com.crow.thread;

import java.util.Set;
import java.util.concurrent.ThreadFactory;

public class ProcessThreadFactory implements ThreadFactory {
    ThreadFactory origin;
    private Set<Thread> threadsContainer;

    public ProcessThreadFactory(ThreadFactory origin, Set<Thread> container) {
        this.origin = origin;
        this.threadsContainer=container;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = origin.newThread(r);
        threadsContainer.add(thread);
        return thread;
    }
}
