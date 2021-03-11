package com.music.services;

import com.music.models.api.Result;
import com.music.models.api.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Service
public class ItunesService implements MusicService{

    @Value("${itunes.service.url}")
    private String itunesServiceUrl;

    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(ItunesService.class);

    @Autowired
    public ItunesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Result> findArtistsByArtistName(String artistName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.valueOf("text/javascript;charset=utf-8")));
//        headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));

        URI uri = UriComponentsBuilder.fromHttpUrl(itunesServiceUrl)
                .pathSegment("search")
                .queryParam("entity", "{entity}")
                .queryParam("term", "{term}")
                .build("allArtist", artistName);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        logger.info("Searching for artists: {}, url: {}", entity, uri.toString());

        HttpEntity<SearchResponse> response =
                restTemplate.exchange(uri, HttpMethod.POST, entity, SearchResponse.class);
        logger.info("Response retrieved: {}", response);

        List<Result> results = response.getBody().getResults();
        return results;
    }
}
