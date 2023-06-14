package com.crow.factory.support;

import com.crow.entity.ProcessPCB;
import com.crow.factory.config.PIDRegistry;

import java.util.Arrays;

public class DefaultPIDRegistry implements PIDRegistry {
    private static final Integer DEFAULT_CAPACITY = 10;
    private boolean[] PIDRecorder = new boolean[DEFAULT_CAPACITY+1];

    public DefaultPIDRegistry() {
        Arrays.fill(PIDRecorder,false);
    }

    @Override
    public Integer getPID() {
        for (int i = 1; i <= DEFAULT_CAPACITY; i++) {
            if(!PIDRecorder[i]){
                PIDRecorder[i]=true;
                return i;
            }
        }
        return null;
    }

    public void destroyProcessPID(ProcessPCB processPCB){
        PIDRecorder[processPCB.getPid()]=false;
    }
}
