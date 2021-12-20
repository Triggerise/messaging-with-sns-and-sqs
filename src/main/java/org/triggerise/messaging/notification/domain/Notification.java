package org.triggerise.messaging.notification.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

    private String id;
    private String sentMessage;
    private String receivedMessage;

}
