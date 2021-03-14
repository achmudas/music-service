package com.music.configurations;

import com.music.exceptions.RestTemplateResponseErrorHandler;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;

@Component
public class Configurations {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     RestTemplateResponseErrorHandler errorHandler) {
        return builder
                .additionalMessageConverters(getMessageConverter())
				.errorHandler(errorHandler)
                .build();
    }

    private Collection<? extends HttpMessageConverter<?>> getMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("text/javascript;charset=utf-8")));
        return Arrays.asList(converter);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplateResponseErrorHandler errorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

}
