package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final String topic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic,
                         KafkaTemplate<String, Object> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String transactionLine) {
        try {
            String[] parts = transactionLine.split(", ");
            long from = Long.parseLong(parts[0].trim());
            long to = Long.parseLong(parts[1].trim());
            float amount = Float.parseFloat(parts[2].trim());

            Transaction transaction = new Transaction(from, to, amount);
            kafkaTemplate.send(topic, transaction).get(); // Wait for send to complete

            System.out.println("SENT: " + transactionLine + " -> Amount: " + amount);
        } catch (Exception e) {
            System.err.println("Failed to send: " + transactionLine + " Error: " + e.getMessage());
        }
    }
}
