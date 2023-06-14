package com.crow.factory;

import com.crow.entity.ProcessPCB;
import com.crow.factory.support.DefaultPIDRegistry;

import java.util.Objects;
import java.util.Random;

import static com.crow.constants.ProcessConstant.MAX_LIFECYCLE;
import static com.crow.constants.ProcessConstant.MAX_PRIORITY;
import static com.crow.enums.ProcessStatusEnum.READY;

public class SimpleProcessFactory extends DefaultPIDRegistry implements ProcessFactory {
    private Random random = new Random();
    @Override
    public ProcessPCB createProcess() throws ProcessOverflowException {
        Integer pid = getPID();
        if(Objects.isNull(pid)){
            throw new ProcessOverflowException("Process is full.");
        }
        return doCreateProcess(pid);
    }

    private ProcessPCB doCreateProcess(Integer pid) {
        Integer priority = random.nextInt(MAX_PRIORITY) + 1;
        Integer lifeCycle = random.nextInt(MAX_LIFECYCLE) + 1;
        return new ProcessPCB(lifeCycle,pid,priority,READY.getStatus());
    }
}
