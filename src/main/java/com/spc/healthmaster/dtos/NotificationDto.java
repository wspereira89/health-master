package com.spc.healthmaster.dtos;

import com.spc.healthmaster.enums.Action;
import com.spc.healthmaster.enums.Status;
import com.spc.healthmaster.enums.TypeStrategy;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificationDto {
    private TypeStrategy typeStrategy;
    private Action action;
    private String applicationId;
    private String serverName;
    private Status status;

}
