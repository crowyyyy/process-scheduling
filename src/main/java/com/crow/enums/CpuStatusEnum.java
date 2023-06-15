package com.crow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CpuStatusEnum {

    RUNNING(0),

    WAITING(1),

    DEAD(2);

    private Integer status;

    public Integer getStatus(){
        return status;
    }
}
