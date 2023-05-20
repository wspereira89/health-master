package com.spc.healthmaster.services.websoket;

import com.spc.healthmaster.dtos.NotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final SimpMessagingTemplate messagingTemplate;


    public MessageServiceImpl(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessageToTopic(final NotificationDto message) {
        messagingTemplate.convertAndSend("/topic/notification", message);
    }
}
