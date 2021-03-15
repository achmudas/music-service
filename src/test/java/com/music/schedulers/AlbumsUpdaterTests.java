package com.music.schedulers;

import com.music.models.internal.Album;
import com.music.models.internal.Artist;
import com.music.services.AlbumsService;
import com.music.services.ArtistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlbumsUpdaterTests {

    private AlbumsUpdater updater;

    @Mock
    private ArtistsService artistsService;
    @Mock
    private AlbumsService albumsService;

    @Captor
    ArgumentCaptor<Artist> artistCaptor;

    @BeforeEach
    public void setUp() {
        this.updater = new AlbumsUpdater(artistsService, albumsService);
    }

    @Test
    void testThatAlbumsAreUpdatedForTheArtistWhoDoesntHaveAny() {
        Artist art1 = new Artist();
        art1.setArtistName("test name");
        art1.setAmgArtistId(55555L);
        List<Artist> artists = Arrays.asList(art1);
        Page<Artist> page = new PageImpl<>(artists);
        when(this.artistsService.getAllArtists(0, 10)).thenReturn(page);
        Map<Long, Set<Album>> albums = getAlbums();
        when(this.albumsService.retrieveAlbums(Arrays.asList(55555L))).thenReturn(albums);
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.of(art1));
        this.updater.updateArtistsAlbums();
        verify(this.artistsService, times(1)).updateArtist(artistCaptor.capture());
        assertThat(artistCaptor.getValue().getAlbums().size()).isEqualTo(2);
        assertThat(artistCaptor.getValue().getAlbums().get(1).getArtistName()).isEqualTo("Someart");
    }

    @Test
    void testIfNoPageableResultFromDB() {
        Page<Artist> page = new PageImpl<>(Arrays.asList());
        when(this.artistsService.getAllArtists(0, 10)).thenReturn(page);
        this.updater.updateArtistsAlbums();
        verify(this.artistsService, times(0)).updateArtist(artistCaptor.capture());
    }



    private Map<Long, Set<Album>> getAlbums() {
        Map<Long, Set<Album>> albums = new HashMap<>();
        Album album1 = new Album();
        album1.setArtistName("Someart");
        album1.setAmgArtistId(55555L);
        album1.setCollectionId(1423802L);
        album1.setCollectionName("Cross Road");
        album1.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        album1.setReleaseDate(new Date());

        Album album2 = new Album();
        album2.setArtistName("Someart");
        album2.setAmgArtistId(55555L);
        album2.setCollectionId(1566802L);
        album2.setCollectionName("Cross Road 2");
        album2.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        album2.setReleaseDate(new Date());
        albums.put(55555L, Set.of(album1, album2));
        return albums;
    }
}
