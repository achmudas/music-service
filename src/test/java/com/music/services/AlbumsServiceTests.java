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

import java.util.*;

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
        Map<Long, Set<Album>> albums = this.albumsService.retrieveAlbums(
                Arrays.asList(55555L, 3734L));
        assertThat(albums.keySet().size()).isEqualTo(2);
        assertThat(albums.get(55555L).size()).isOne();
        assertThat(albums.get(3734L).size()).isEqualTo(2);
    }

    @Test
    void testThatNoDuplicateAlbumsAreAdded() {
        List<Result> results = getResults();
        Result res5 = new Result();
        res5.setArtistName("Someart");
        res5.setAmgArtistId(55555L);
        res5.setCollectionId(1423284802L);
        res5.setCollectionName("Cross Road");
        res5.setCollectionType(CollectionType.ALBUM);
        res5.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        res5.setReleaseDate(results.get(0).getReleaseDate());
        res5.setWrapperType(WrapperType.COLLECTION);
        results.add(res5);
        when(musicService.retrieveAlbumsForArtist(anyList())).thenReturn(results);
        Map<Long, Set<Album>> albums = this.albumsService.retrieveAlbums(
                Arrays.asList(55555L, 3734L));
        assertThat(albums.keySet().size()).isEqualTo(2);
        assertThat(albums.get(55555L).size()).isOne();
        assertThat(albums.get(3734L).size()).isEqualTo(2);
    }

    @Test
    void testThatNoMoreThan5AlbumsAreSetForArtist() {
        List<Result> results = getResults();
        for (Result result : results) {
            result.setAmgArtistId(55555L);
            result.setCollectionId(new Random().nextLong());
        }
        Result res5 = new Result();
        res5.setArtistName("Someart");
        res5.setAmgArtistId(55555L);
        res5.setCollectionId(1423802L);
        res5.setCollectionName("Cross Road");
        res5.setCollectionType(CollectionType.ALBUM);
        res5.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        res5.setReleaseDate(new Date());
        res5.setWrapperType(WrapperType.COLLECTION);
        results.add(res5);
        Result res6 = new Result();
        res6.setArtistName("Someart");
        res6.setAmgArtistId(55555L);
        res6.setCollectionId(14232848L);
        res6.setCollectionName("Album 6");
        res6.setCollectionType(CollectionType.ALBUM);
        res6.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        res6.setReleaseDate(new Date());
        res6.setWrapperType(WrapperType.COLLECTION);
        results.add(res6);
        when(musicService.retrieveAlbumsForArtist(anyList())).thenReturn(results);
        Map<Long, Set<Album>> albums = this.albumsService.retrieveAlbums(
                Arrays.asList(55555L, 3734L));
        assertThat(albums.get(55555L).size()).isEqualTo(5);
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
        res3.setCollectionId(440855519L);
        res3.setCollectionName("What About Now (Deluxe Version)");
        res3.setCollectionType(CollectionType.ALBUM);
        res3.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-dfdfff5eeac9/source/100x100bb.jpg");
        res3.setReleaseDate(new Date());
        res3.setWrapperType(WrapperType.COLLECTION);

        Result res4 = new Result();
        res4.setArtistName("Someart");
        res4.setAmgArtistId(3734L);
        res4.setCollectionId(144555569L);
        res4.setCollectionName("Some other album");
        res4.setCollectionType(CollectionType.ALBUM);
        res4.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7/62/7a076261-dfdfff5eeac9/source/100x100bb.jpg");
        res4.setReleaseDate(new Date());
        res4.setWrapperType(WrapperType.COLLECTION);

        List<Result> results = new ArrayList<>();
        results.add(res1);
        results.add(res2);
        results.add(res3);
        results.add(res4);
        return results;
    }
}
