package br.com.batista.desafio01.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "notify", groupId = "desafio01")
    public void listen(String message) {
        logger.debug("Received message: ".concat(message));
    }
}