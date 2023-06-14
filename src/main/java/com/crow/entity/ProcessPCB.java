package com.crow.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@ToString
public class ProcessPCB implements Comparable<ProcessPCB> {
    /**
     * 生命周期 1-5
     */
    private Integer life;
    /**
     * 进程标识号 1-100
     */
    private Integer pid;
    /**
     * 进程优先级 0-49
     */
    private Integer priority;
    /**
     * 进程状态 0/1
     */
    private Integer status;

    @Override
    public int compareTo(ProcessPCB o) {
        return o.priority-this.priority;
    }

    public void nextLifeCycle(){
        this.priority = priority/2;
        this.life--;
    }

    public String getStatus(){
        return status.equals(1)?"run":"wait";
    }
}
