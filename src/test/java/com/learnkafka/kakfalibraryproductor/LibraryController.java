package com.learnkafka.kakfalibraryproductor;

import com.learnkafka.kakfalibraryproductor.dto.LibraryEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LibraryController {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void postLibraryEvent() {

        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(TestUtil.libraryEventRecord());

        var response = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,
                httpEntity, LibraryEvent.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
