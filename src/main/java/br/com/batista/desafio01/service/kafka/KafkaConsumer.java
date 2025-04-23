package br.com.batista.desafio01.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

//@Service
public class KafkaConsumer {

    @KafkaListener(topics = "notify", groupId = "desafio01")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}