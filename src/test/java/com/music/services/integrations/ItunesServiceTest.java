package com.music.services.integrations;

import com.music.models.external.Result;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(ItunesService.class)
public class ItunesServiceTest {

    @Autowired
    private MusicService musicService;

    @Autowired
    private MockRestServiceServer server;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    void testSearchQueryParamIsEncoded() {

    }

    @Test
    void testWhenNothingIsReturned() {

    }

    @Test
    void testWhenEmptyResultIsReturned() {
        String searchResponse = "{\n" +
                " \"resultCount\":0,\n" +
                " \"results\": []\n" +
                "}";

        this.server.expect(requestTo("http://localhost:8080/search?entity=allArtist&term=erererer"))
                .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));

        List<Result> result = this.musicService.findArtistsByArtistName("erererer");

        assertThat(result.isEmpty()).isTrue();
    }
}
