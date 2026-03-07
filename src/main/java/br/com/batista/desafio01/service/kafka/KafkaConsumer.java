package br.com.batista.desafio01.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "notify", groupId = "desafio01")
    public void listen(String message) {
        logger.atDebug().log(() -> "Received message: " + message);
    }
}