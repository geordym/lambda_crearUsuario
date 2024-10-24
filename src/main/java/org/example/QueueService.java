package org.example;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QueueService {
public static void sendMessage(Usuario usuario) {
    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    // Nombre de la cola
    String SQS_NAME = "UsuarioSQS_test";

    // Obtén la URL de la cola usando el nombre
    String queueUrl = sqs.getQueueUrl(SQS_NAME).getQueueUrl();

    String template = "Se ha creado un usuario de nombre %s cuyo email es %s en esta fecha %s, por favor compruebelo";

    // Formateo de la fecha
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = LocalDateTime.now().format(formatter);

    // Usa String.format con los tipos correctos
    String messageToSend = String.format(template, usuario.getNombre(), usuario.getEmail(), formattedDate);

    SendMessageRequest send_msg_request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(messageToSend)
            .withDelaySeconds(5);
    sqs.sendMessage(send_msg_request);

    List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

    for (Message m : messages) {
        sqs.deleteMessage(queueUrl, m.getReceiptHandle());
    }
}
}
