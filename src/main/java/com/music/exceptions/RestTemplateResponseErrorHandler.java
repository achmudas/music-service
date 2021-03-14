package com.music.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().isError();
    }

    /**
     * Handles error response returned from @{@link com.music.services.integrations.MusicService}.
     * Perfect place to implement error deserialization and return specific exceptions
     * with particular error response which can be used in API.
     * @param httpResponse
     * @throws IOException
     */
    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        logger.error("Request failed, response code: {}, response: {}", httpResponse.getStatusCode(), httpResponse.getStatusText());
        throw new GeneralResponseException(
                "Request failed, response code: + " + httpResponse.getStatusCode()
                        + ", response: " + httpResponse.getStatusText()
        );
    }

}