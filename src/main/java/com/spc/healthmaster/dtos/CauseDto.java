package com.spc.healthmaster.dtos;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@ApiModel(description = "Api Error")
public class CauseDto {
    private String code;
    private String description;
}
