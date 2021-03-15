package com.music.schedulers;

import com.music.models.internal.AlbumEntity;
import com.music.models.internal.ArtistEntity;
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
    ArgumentCaptor<ArtistEntity> artistCaptor;

    @BeforeEach
    public void setUp() {
        this.updater = new AlbumsUpdater(artistsService, albumsService);
    }

    @Test
    void testThatAlbumsAreUpdatedForTheArtistWhoDoesntHaveAny() {
        ArtistEntity art1 = new ArtistEntity();
        art1.setArtistName("test name");
        art1.setAmgArtistId(55555L);
        List<ArtistEntity> artists = Arrays.asList(art1);
        Page<ArtistEntity> page = new PageImpl<>(artists);
        when(this.artistsService.getAllArtists(0, 10)).thenReturn(page);
        Map<Long, Set<AlbumEntity>> albums = getAlbums();
        when(this.albumsService.retrieveAlbums(Arrays.asList(55555L))).thenReturn(albums);
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.of(art1));
        this.updater.updateArtistsAlbums();
        verify(this.artistsService, times(1)).updateArtist(artistCaptor.capture());
        assertThat(artistCaptor.getValue().getAlbums().size()).isEqualTo(2);
        assertThat(artistCaptor.getValue().getAlbums().get(1).getArtistName()).isEqualTo("Someart");
    }

    @Test
    void testIfNoPageableResultFromDB() {
        Page<ArtistEntity> page = new PageImpl<>(Arrays.asList());
        when(this.artistsService.getAllArtists(0, 10)).thenReturn(page);
        this.updater.updateArtistsAlbums();
        verify(this.artistsService, times(0)).updateArtist(artistCaptor.capture());
    }

    @Test
    void testThatWithMultipleUsersIsWorking() {
        ArtistEntity art1 = new ArtistEntity();
        art1.setArtistName("test name");
        art1.setAmgArtistId(55555L);
        ArtistEntity art2 = new ArtistEntity();
        art2.setArtistName("test name 2");
        art2.setAmgArtistId(55556L);
        Page<ArtistEntity> page1 = new PageImpl<>(Arrays.asList(art1, art2));
        when(this.artistsService.getAllArtists(0, 10)).thenReturn(page1);
        Map<Long, Set<AlbumEntity>> albums = getAlbums();
        AlbumEntity album3 = new AlbumEntity();
        album3.setArtistName("test name 2");
        album3.setAmgArtistId(55556L);
        album3.setCollectionId(102L);
        album3.setCollectionName("BlaBla");
        album3.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        album3.setReleaseDate(new Date());
        albums.put(55556L, Set.of(album3));
        when(this.albumsService.retrieveAlbums(Arrays.asList(55555L, 55556L))).thenReturn(albums);
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.empty());
        when(this.artistsService.findArtist(55556L)).thenReturn(Optional.of(art2));
        this.updater.updateArtistsAlbums();
        verify(this.artistsService, times(1)).updateArtist(artistCaptor.capture());
        assertThat(artistCaptor.getValue().getAlbums().size()).isEqualTo(1);
        assertThat(artistCaptor.getValue().getAlbums().get(0).getCollectionId()).isEqualTo(102L);
    }

    private Map<Long, Set<AlbumEntity>> getAlbums() {
        Map<Long, Set<AlbumEntity>> albums = new HashMap<>();
        AlbumEntity album1 = new AlbumEntity();
        album1.setArtistName("Someart");
        album1.setAmgArtistId(55555L);
        album1.setCollectionId(1423802L);
        album1.setCollectionName("Cross Road");
        album1.setArtworkUrl100("https://is4-ssl.mzstatic.com/image/thumb/Music124/" +
                "v4/7a/07/62/7a076261-23f9-8846-1d65-0ecd045eeac9/source/100x100bb.jpg");
        album1.setReleaseDate(new Date());

        AlbumEntity album2 = new AlbumEntity();
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
