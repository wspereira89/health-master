package com.spc.healthmaster.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Api Error")
public enum Status {
    @ApiModelProperty(value = "RUNNING status", example = "RUNNING")
    RUNNING,
    @ApiModelProperty(value = "STOPPED status", example = "STOPPED")
    STOPPED,
    @ApiModelProperty(value = "UNDEFINED status", example = "UNDEFINED")
    UNDEFINED
}
