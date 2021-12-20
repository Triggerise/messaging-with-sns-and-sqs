package org.triggerise.messaging.notification.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.triggerise.messaging.notification.domain.Notification;
import org.triggerise.messaging.notification.domain.NotificationRequest;
import org.triggerise.messaging.notification.service.NotificationReceiver;
import org.triggerise.messaging.notification.service.NotificationSender;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationSender notificationSender;
    private final NotificationReceiver notificationReceiver;

    @PostMapping("")
    public ResponseEntity<Notification> sendNotification(@RequestBody NotificationRequest notificationRequest) {
        Notification sentNotification = notificationSender.sendNotification(notificationRequest);
        Notification receivedNotification = notificationReceiver.receiveNotification(sentNotification);

        return ResponseEntity.ok(receivedNotification);
    }

}
