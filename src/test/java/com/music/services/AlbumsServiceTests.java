package com.music.services;

import com.music.models.external.CollectionType;
import com.music.models.external.Result;
import com.music.models.external.WrapperType;
import com.music.models.internal.Album;
import com.music.services.integrations.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumsServiceTests {

    private AlbumsService albumsService;

    @Mock
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        this.albumsService = new AlbumsService(musicService, new ModelMapper());
    }

    @Test
    void testThatAlbumsAreCollectedAccordingAmgArtistId() {
        List<Result> results = getResults();
        when(musicService.retrieveAlbumsForArtist(anyList())).thenReturn(results);
        Map<Long, List<Album>> albums = this.albumsService.retrieveAlbums(
                Arrays.asList(55555L, 3734L));
        assertThat(albums.keySet().size()).isEqualTo(2);
        assertThat(albums.get(55555L).size()).isOne();
        assertThat(albums.get(3734L).size()).isEqualTo(2);
    }

    private List<Result> getResults () {
        Result res1 = new Result();
        res1.setArtistName("Someart");
        res1.setAmgArtistId(55555L);
        res1.setCollectionId(1423284802L);
        res1.setCollectionName("Cross Road");
        res1.setCollectionType(CollectionType.ALBUM);
        res1.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        res1.setReleaseDate(new Date());
        res1.setWrapperType(WrapperType.COLLECTION);

        Result res2 = new Result();
        res2.setArtistName("Bon Jovi");
        res2.setAmgArtistId(3734L);
        res2.setWrapperType(WrapperType.ARTIST);

        Result res3 = new Result();
        res3.setArtistName("Someart");
        res3.setAmgArtistId(3734L);
        res3.setCollectionId(1440855519L);
        res3.setCollectionName("What About Now (Deluxe Version)");
        res3.setCollectionType(CollectionType.ALBUM);
        res3.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-dfdfff5eeac9/source/100x100bb.jpg");
        res3.setReleaseDate(new Date());
        res3.setWrapperType(WrapperType.COLLECTION);

        Result res4 = new Result();
        res4.setArtistName("Someart");
        res4.setAmgArtistId(3734L);
        res4.setCollectionId(14455519L);
        res4.setCollectionName("Some other album");
        res4.setCollectionType(CollectionType.ALBUM);
        res4.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7/62/7a076261-dfdfff5eeac9/source/100x100bb.jpg");
        res4.setReleaseDate(new Date());
        res4.setWrapperType(WrapperType.COLLECTION);

        return Arrays.asList(res1, res2, res3, res4);
    }
}
