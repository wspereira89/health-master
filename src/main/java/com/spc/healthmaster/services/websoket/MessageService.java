package com.spc.healthmaster.services.websoket;

import com.spc.healthmaster.dtos.NotificationDto;

public interface MessageService {

    void sendMessageToTopic(final NotificationDto notificationDto);
}
