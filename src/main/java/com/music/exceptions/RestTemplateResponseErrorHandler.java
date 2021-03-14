package com.ibm.acoustic.content.manager;

import com.ibm.acoustic.content.manager.exceptions.ErrorDeserializer;
import com.ibm.acoustic.content.manager.exceptions.GeneralResponseException;
import com.ibm.acoustic.content.manager.exceptions.STCResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    private ErrorDeserializer errorDeserializer;

    @Autowired
    public RestTemplateResponseErrorHandler(ErrorDeserializer errorDeserializer) {
        this.errorDeserializer = errorDeserializer;
    }

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getBody() != null && httpResponse.getBody().available() > 0) {
            handleSTCErrors(httpResponse);
        }
        handleStandardErrors(httpResponse);
    }

    private void handleStandardErrors(ClientHttpResponse httpResponse) throws IOException {
        logger.error("Request failed, response code: {}, response: {}", httpResponse.getStatusCode(), httpResponse.getStatusText());
        throw new GeneralResponseException(
                "Request failed, response code: + " + httpResponse.getStatusCode()
                + ", response: " + httpResponse.getStatusText()
        );
    }

    private void handleSTCErrors(ClientHttpResponse httpResponse) throws IOException {
        try {
            STCResponseException ex = errorDeserializer.deserialize(httpResponse.getBody(), STCResponseException.class);
            logger.error("Request failed, error status: {}, message: {}", ex.getStatus(), ex.getMessage());
            throw ex;
        } catch (IOException ex) {
            logger.error("Request failed, error code: {}, message: {}", httpResponse.getStatusCode(), httpResponse.getStatusText());
            throw new GeneralResponseException(ex,
                    "Deserialization of error response failed. Response error code: " + httpResponse.getStatusCode() +
                    ", response: " + httpResponse.getStatusText());
        }
    }
}