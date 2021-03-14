package com.music.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RestTemplateResponseErrorHandlerTests {

    private RestTemplateResponseErrorHandler handler;

    @BeforeEach
    public void setUp() {
        this.handler = new RestTemplateResponseErrorHandler();
    }

    @Test
    void testExceptionIsThrown() throws Exception{
        ClientHttpResponse httpResponse = new MockClientHttpResponse(
                "Failed to retrieve details".getBytes(StandardCharsets.UTF_8),
                HttpStatus.INTERNAL_SERVER_ERROR);
        assertThrows(GeneralResponseException.class, () -> handler.handleError(httpResponse));
    }
}
