package com.learnkafka.kakfalibraryproductor.dto;

public record Book(
        Integer bookId,
        String bookName,
        String  bookAuthor
) {
}
