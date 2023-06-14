package com.crow.factory;

import com.crow.entity.ProcessPCB;

public interface ProcessFactory {

    ProcessPCB createProcess() throws ProcessOverflowException;
}
