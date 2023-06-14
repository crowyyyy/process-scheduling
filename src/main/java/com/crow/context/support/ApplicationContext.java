package com.crow.context.support;

import com.crow.entity.ProcessPCB;
import com.crow.factory.ProcessOverflowException;
import com.crow.factory.SimpleProcessFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crow.constants.ProcessConstant.DEAD;

public class ApplicationContext {
    private volatile PriorityQueue<ProcessPCB> queue = new PriorityQueue<>();
    private SimpleProcessFactory processFactory = new SimpleProcessFactory();

    public void createProcess() throws ProcessOverflowException {
        ProcessPCB processPCB = processFactory.createProcess();
        queue.add(processPCB);
    }
    public ProcessPCB fetchProcess(){
        return queue.poll();
    }

    /**
     * @param processPCB
     * @return whether the process is destroyed after this slice
     */
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

    public List<ProcessPCB> getPriorityQueue(){
        Object[] array =  queue.toArray();
        Arrays.sort(array);
        return Stream.of(array).map(ele->(ProcessPCB)ele).collect(Collectors.toList());
    }

}
