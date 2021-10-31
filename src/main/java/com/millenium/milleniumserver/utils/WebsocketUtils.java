package com.millenium.milleniumserver.utils;

import com.millenium.milleniumserver.entity.auth.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebsocketUtils {
    private SimpMessagingTemplate simpMessagingTemplate;

    public void sendMessageToUsers(List<UserEntity> users, String destination, Object payload) {
        for (UserEntity userEntity : users) {
            simpMessagingTemplate.convertAndSendToUser(userEntity.getUsername(), destination, payload);
            //simpMessagingTemplate.convertAndSendToUser(userEntity.getUsername(), destination, "payload");
        }
    }

    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
}
