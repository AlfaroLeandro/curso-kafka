package com.learnkafka.kakfalibraryproductor.controlador;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.kakfalibraryproductor.dto.LibraryEvent;
import com.learnkafka.kakfalibraryproductor.producer.LibraryEventsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LibraryEventsController {
    private Logger logger = LoggerFactory.getLogger(LibraryEventsController.class);

    @Autowired
    private LibraryEventsProducer libraryEventsProducer;

    @PostMapping("/v1/libraryevent")
    public Mono<ResponseEntity<LibraryEvent>> postLibraryEvent(
            @RequestBody LibraryEvent libraryEvent
    ) throws JsonProcessingException {
        return libraryEventsProducer
                .sendLibraryEvent(libraryEvent)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED)
                                                .body(libraryEvent)))
                .doOnNext(e -> logger.info("libraryEvent: {}", e));
    }

}
