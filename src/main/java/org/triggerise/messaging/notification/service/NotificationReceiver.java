package org.triggerise.messaging.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.triggerise.messaging.notification.domain.Notification;
import org.triggerise.messaging.notification.domain.NotificationResponse;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

@Slf4j
@Service
public class NotificationReceiver {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${sqs.queue.url}")
    private String queueUrl;

    private SqsClient sqsClient;

    public Notification receiveNotification(Notification notification) {
        try {
            init();
            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1)
                    .build();
            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            int lastMessageIndex = messages.size() - 1;
            Message message = messages.get(lastMessageIndex);
            NotificationResponse notificationResponse = NotificationResponse.builder()
                    .messageId(message.messageId())
                    .body(message.body())
                    .build();

            notification.setReceivedMessage(notificationResponse.getBody());
        } catch (SqsException exception) {
            log.error(exception.awsErrorDetails().errorMessage());
        }

        return notification;
    }

    private void init() {
        sqsClient = SqsClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }

}
