package com.music.services.integrations;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItunesServiceTests {
//    #FIXME
//
//    @Autowired
//    private ItunesService musicService;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private MockRestServiceServer server;
//
//
//    @Before
//    public void setUp() throws Exception {
//        server = MockRestServiceServer.createServer(restTemplate);
//    }
//
//    @Test
//    public void testSearchQueryParamIsEncoded() {
//
//    }
//
//    @Test
//    public void testWhenNothingIsReturned() {
//
//    }
//
//    @Test
//    public void testWhenEmptyResultIsReturned() {
//        String searchResponse = "{\n" +
//                " \"resultCount\":0,\n" +
//                " \"results\": []\n" +
//                "}";
//
//        this.server.expect(requestTo("http://localhost:8080/search?entity=allArtist&term=erererer"))
//                .andRespond(withSuccess(searchResponse, MediaType.APPLICATION_JSON));
//
//        List<Result> result = this.musicService.findArtistsByArtistName("erererer");
//        assertThat(result.isEmpty()).isTrue();
//        this.server.verify();
//    }
}
