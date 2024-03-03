package com.learnkafka.kakfalibraryproductor.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.kakfalibraryproductor.dto.LibraryEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class LibraryEventsProducer {
    private Logger logger = LoggerFactory.getLogger(LibraryEventsProducer.class);

    @Value("${spring.kafka.topic}")
    public String topic;

    private KafkaTemplate<Integer, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public LibraryEventsProducer(KafkaTemplate<Integer, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<SendResult<Integer, String>> sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        var key = libraryEvent.libraryEventId();
        var value = objectMapper.writeValueAsString(libraryEvent);

        return Mono.fromFuture(kafkaTemplate.send(topic, key, value))
                        .doOnNext(sendResult -> handleSuccess(key, value, sendResult))
                        .doOnError(ex -> handleFailure(key, value, ex));
    }

//    public Mono<SendResult<Integer, String>> sendLibraryEventProducerRecord(LibraryEvent libraryEvent) throws JsonProcessingException {
//        var key = libraryEvent.libraryEventId();
//        var value = objectMapper.writeValueAsString(libraryEvent);
//
//        var headers = List.of(new RecordHeader("event-source", "scanner".getBytes()));
//        var producerRecord = new ProducerRecord<Integer, String>(topic, null, key, value, headers);
//
//        return Mono.fromFuture(kafkaTemplate.send(producerRecord))
//                        .doOnNext(sendResult -> handleSuccess(key, value, sendResult))
//                        .doOnError(ex -> handleFailure(key, value, ex));
//    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult) {
        logger.info("Mensaje enviado exitosamente, key: {}, valor: {}, particion: {}", key, value , sendResult.getRecordMetadata().partition());
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        logger.error("Error enviando el mensaje. Exception: {}", ex.getMessage(), ex);
    }

}
