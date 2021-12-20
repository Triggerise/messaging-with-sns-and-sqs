package org.triggerise.messaging.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.triggerise.messaging.notification.domain.Notification;
import org.triggerise.messaging.notification.domain.NotificationRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

@Slf4j
@Service
public class NotificationSender {

    @Value("${sns.topic.arn}")
    private String topicArn;

    @Value("${aws.region}")
    private String awsRegion;

    private SnsClient snsClient;

    public Notification sendNotification(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .sentMessage(notificationRequest.getMessage())
                .build();

        try {
            init();
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(notificationRequest.getMessage())
                    .topicArn(topicArn)
                    .build();
            PublishResponse publishResponse = snsClient.publish(publishRequest);

            log.info("Message with id " + publishResponse.messageId() + " sent. Status is " + publishResponse.sdkHttpResponse().statusCode());
            notification.setId(publishResponse.messageId());
        } catch (SnsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }

        return notification;
    }

    private void init() {
        snsClient = SnsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
