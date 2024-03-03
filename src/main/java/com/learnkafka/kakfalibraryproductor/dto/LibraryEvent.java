package com.learnkafka.kakfalibraryproductor.dto;

import com.learnkafka.kakfalibraryproductor.persistencia.entidad.enumerable.LibraryEventType;

public record LibraryEvent(
        Integer libraryEventId,
        LibraryEventType libraryEventType,
        Book book
) {
}
