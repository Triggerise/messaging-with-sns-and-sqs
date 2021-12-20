package org.triggerise.messaging.notification.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {

    private String messageId;
    private String body;

}
