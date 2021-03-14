package com.music.services.integrations;

import com.music.models.external.Result;
import com.music.models.external.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        URI uri = UriComponentsBuilder.fromHttpUrl(itunesServiceUrl)
                .pathSegment("search")
                .queryParam("entity", "{entity}")
                .queryParam("term", "{term}")
                .build("allArtist", artistName);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        logger.info("Searching for artists: {}, url: {}", entity, uri);

        HttpEntity<SearchResponse> response =
                restTemplate.exchange(uri, HttpMethod.POST, entity, SearchResponse.class);
        logger.info("Response retrieved: {}", response);

        return (response.getBody() != null && response.getBody().getResults() != null) ?
                response.getBody().getResults() :
                new ArrayList<>();
    }

    @Override
    public Optional<Result> findArtistsByAmgArtistId(Long artistId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.valueOf("text/javascript;charset=utf-8")));

        URI uri = UriComponentsBuilder.fromHttpUrl(itunesServiceUrl)
                .pathSegment("lookup")
                .queryParam("amgArtistId", "{amgArtistId}")
                .build(artistId);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        logger.info("Searching for artist with id: {}, url: {}", artistId, uri);

        HttpEntity<SearchResponse> response =
                restTemplate.exchange(uri, HttpMethod.GET, entity, SearchResponse.class);
        logger.info("Response retrieved: {}", response);

        return response.getBody() != null &&
                !CollectionUtils.isEmpty(response.getBody().getResults()) ?
                Optional.of(response.getBody().getResults().get(0)) :
                Optional.empty();
    }

    @Override
    public List<Result> retrieveAlbumsForArtist(List<Long> amgArtistIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.valueOf("text/javascript;charset=utf-8")));

        String listOfAmgArtistIds = StringUtils.collectionToCommaDelimitedString(amgArtistIds);

        URI uri = UriComponentsBuilder.fromHttpUrl(itunesServiceUrl)
                .pathSegment("lookup")
                .queryParam("amgArtistId", "{amgArtistIds}")
                .queryParam("entity", "{entity}")
                .queryParam("limit", "{limit}")
                .build(listOfAmgArtistIds, "album", 5);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        logger.info("Retrieving albums for artists with ids: {}, url: {}", listOfAmgArtistIds, uri);

        HttpEntity<SearchResponse> response =
                restTemplate.exchange(uri, HttpMethod.POST, entity, SearchResponse.class);
        logger.info("Response retrieved: {}", response);

        return response.getBody() != null && response.getBody().getResults() != null ?
                response.getBody().getResults() :
                new ArrayList<>();
    }
}
