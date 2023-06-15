package com.crow.context.support;

import com.crow.context.event.DestroyNoticeRenderEvent;
import com.crow.context.event.DialogRenderEvent;
import com.crow.context.event.ScheduleNoticeRenderEvent;
import com.crow.entity.ProcessPCB;
import com.crow.enums.CpuStatusEnum;
import com.crow.factory.ProcessOverflowException;
import com.crow.factory.support.SimpleProcessFactory;
import com.crow.thread.ProcessThreadFactory;
import javafx.application.Platform;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crow.constants.ProcessConstant.DEAD;
import static com.crow.constants.ProcessConstant.TIME_SLICE_SIZE;
import static com.crow.enums.CpuStatusEnum.RUNNING;
import static com.crow.enums.CpuStatusEnum.WAITING;

public class AbstractRunnableApplicationContext extends AbstractObservableApplicationContext{
    private ProcessPCB currentPCB;
    private volatile PriorityQueue<ProcessPCB> queue = new PriorityQueue<>();
    private SimpleProcessFactory processFactory = new SimpleProcessFactory();
    private ThreadPoolExecutor executor;
    private Set<Thread> threadsContainer= new HashSet<>();
    private volatile Integer status;
    protected void initProcessConsumer() {
        status = RUNNING.getStatus();
        executor.execute(()->{
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+Thread.currentThread().getName()+"正在等待进程");
                run();
                if(isInterrupted()){
                    LockSupport.park();
                }
                if(isDead()){
                    break;
                }
            }
        });
    }

    private boolean isDead() {
        return status.equals(CpuStatusEnum.DEAD.getStatus());
    }
    private boolean isInterrupted(){
        return status.equals(WAITING);
    }
    protected void initThreadExecutor(){
        executor =new ThreadPoolExecutor(2,
                2,
                1,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue());
        ThreadFactory originalThreadFactory = executor.getThreadFactory();
        executor.setThreadFactory(new ProcessThreadFactory(originalThreadFactory,threadsContainer));
    }

    private void run(){
        ProcessPCB processPCB = fetchProcess();
        if(Objects.isNull(processPCB)){
            return;
        }
        currentPCB = processPCB;
        List<ProcessPCB> pcbList = listPriorityQueue();
        render(pcbList);
        schedule(currentPCB);
        try {
            System.out.println("PID为"+currentPCB.getPid()+"的进程获取时间片");
            Thread.sleep(TIME_SLICE_SIZE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(afterRun(processPCB)){
            destroy(processPCB);
        }

    }

    private void schedule(ProcessPCB currentPCB) {
        publishEvent(new ScheduleNoticeRenderEvent(this,currentPCB));
    }

    private void destroy(ProcessPCB processPCB) {
        publishEvent(new DestroyNoticeRenderEvent(this,currentPCB));
    }

    public ProcessPCB fetchProcess(){
        return queue.poll();
    }

    private void render(List<ProcessPCB> pcbList){
        publishEvent(new DialogRenderEvent(this,pcbList));
    }

    public boolean afterRun(ProcessPCB processPCB){
        processPCB.nextLifeCycle();
        if(processPCB.getLife().equals(DEAD)){
            destroyProcess(processPCB);
            return true;
        }else{
            queue.add(processPCB);
            return false;
        }
    }
    private void destroyProcess(ProcessPCB processPCB) {
        processFactory.destroyProcessPID(processPCB);
    }

    public List<ProcessPCB> listPriorityQueue(){
        Object[] array =  queue.toArray();
        Arrays.sort(array);
        return Stream.of(array).map(ele->(ProcessPCB)ele).collect(Collectors.toList());
    }

    @Override
    public void stop() {
        Platform.exit();
        status = CpuStatusEnum.DEAD.getStatus();
    }

    @Override
    public void pause() {
        status = WAITING.getStatus();
    }

    @Override
    public void addProcess() throws ProcessOverflowException {
        ProcessPCB processPCB = processFactory.createProcess();
        queue.add(processPCB);
        render(listPriorityQueue());
    }

    @Override
    public void resume() {
        status = RUNNING.getStatus();
        for (Thread thread : threadsContainer) {
            LockSupport.unpark(thread);
        }
    }
}
