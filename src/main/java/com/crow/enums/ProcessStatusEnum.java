package com.crow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProcessStatusEnum {
    READY(0),

    RUN(1);

    private Integer status;
}
