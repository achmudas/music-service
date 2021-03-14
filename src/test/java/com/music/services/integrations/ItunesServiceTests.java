package com.music.services.integrations;

import com.music.exceptions.GeneralResponseException;
import com.music.models.external.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

@SpringBootTest
public class ItunesServiceTests {

    @Autowired
    private ItunesService musicService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer server;


    @BeforeEach
    public void setUp() throws Exception {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testSearchQueryParamIsEncoded() {

    }

    @Test
    public void testWhenNothingIsReturned() {
        String searchResponse = "";

        this.server.expect(requestTo("http://localhost:8080/search?entity=allArtist&term=erererer"))
                .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));

        List<Result> result = this.musicService.findArtistsByArtistName("erererer");
        assertThat(result.isEmpty()).isTrue();
        this.server.verify();
    }

    @Test
    public void testWhenEmptyResultIsReturned() {
        String searchResponse = "{\n" +
                " \"resultCount\":0,\n" +
                " \"results\": []\n" +
                "}";

        this.server.expect(requestTo("http://localhost:8080/search?entity=allArtist&term=erererer"))
                .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));

        List<Result> result = this.musicService.findArtistsByArtistName("erererer");
        assertThat(result.isEmpty()).isTrue();
        this.server.verify();
    }

    @Test
    void testThatExternalErrorIsHandled() {
        this.server
                .expect(requestTo(
                        "http://localhost:8080/lookup?amgArtistId=4444"))
                .andRespond(withUnauthorizedRequest());
        assertThatThrownBy(() -> {
            this.musicService.findArtistsByAmgArtistId(4444L);
        })
                .isInstanceOf(GeneralResponseException.class)
                .hasMessageContaining("Request failed, response code: + 401 UNAUTHORIZED, response: Unauthorized");

        this.server.verify();
    }

    @Test
    void testThatListOfIdsAreMergedAndAlbumsAreReturned() {
        String response = "{\n" +
                " \"resultCount\":2,\n" +
                " \"results\": [\n" +
                "{\"wrapperType\":\"artist\", \"artistType\":\"Artist\", \"artistName\":\"Harvey Schmidt\", " +
                "\"artistLinkUrl\":\"https://music.apple.com/us/artist/harvey-schmidt/216259?uo=4\", " +
                "\"artistId\":216259, \"amgArtistId\":172607, \"primaryGenreName\":\"Soundtrack\", \"primaryGenreId\":16}, \n" +
                "{\"wrapperType\":\"collection\", \"collectionType\":\"Album\", \"artistId\":216259, " +
                "\"collectionId\":1303814134, \"amgArtistId\":172607, \"artistName\":\"Harvey Schmidt & Tom Jones\", " +
                "\"collectionName\":\"The Fantasticks (The New Off-Broadway Recording)\", \"collectionCensoredName\":" +
                "\"The Fantasticks (The New Off-Broadway Recording)\", " +
                "\"artistViewUrl\":\"https://music.apple.com/us/artist/harvey-schmidt/216259?uo=4\", " +
                "\"collectionViewUrl\":\"https://music.apple.com/us/album/the-fantasticks-the-new-off-broadway-recording/1303814134?uo=4\", " +
                "\"artworkUrl60\":\"https://is3-ssl.mzstatic.com/image/thumb/Music128/v4/60/de/36/60de36ef-ae26-3858-8302-742a42eebda9/source/60x60bb.jpg\", " +
                "\"artworkUrl100\":\"https://is3-ssl.mzstatic.com/image/thumb/Music128/v4/60/de/36/60de36ef-ae26-3858-8302-742a42eebda9/source/100x100bb.jpg\", " +
                "\"collectionPrice\":11.99, \"collectionExplicitness\":\"notExplicit\", \"trackCount\":28, \"copyright\":\"℗ 2006 Sh-K-Boom Records, Inc.\", \"country\":\"USA\", \"currency\":\"USD\", \"releaseDate\":" +
                "\"2006-11-14T08:00:00Z\", \"primaryGenreName\":\"Soundtrack\"}]}";

        this.server.expect(requestTo("http://localhost:8080/lookup?amgArtistId=323%2C2323%2C11&entity=album&limit=5"))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        List<Result> results = this.musicService.retrieveAlbumsForArtist(Arrays.asList(323L, 2323L, 11L));
        assertThat(results.size()).isEqualTo(2);
        assertThat(results.get(1).getCollectionId()).isEqualTo(1303814134L);
    }
}
