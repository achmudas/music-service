package com.music.controllers;

import com.music.models.api.Album;
import com.music.models.api.Artist;
import com.music.models.internal.AlbumEntity;
import com.music.models.internal.ArtistEntity;
import com.music.services.ArtistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistsControllerTests {

    private ArtistsController controller;

    @Mock
    private ArtistsService artistsService;

    @BeforeEach
    public void setUp() {
        this.controller = new ArtistsController(artistsService, new ModelMapper());
    }

    @Test
    void testThatArtistEntitiesAreMappedToArtistAndReturned() {
        String artistName = "jon";
        ArtistEntity art1 = new ArtistEntity();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        ArtistEntity art2 = new ArtistEntity();
        art2.setArtistName("Jon Something");
        art2.setAmgArtistId(4444L);
        when(this.artistsService.findArtists(artistName)).thenReturn(Arrays.asList(art1, art2));
        ResponseEntity<List<Artist>> response = this.controller.findArtists(artistName);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).getAmgArtistId()).isEqualTo(4444L);
    }

    @Test
    void testThatNoArtistsAreFound() {
        String artistName = "jon";
        when(this.artistsService.findArtists(artistName)).thenReturn(Arrays.asList());
        ResponseEntity<List<Artist>> response = this.controller.findArtists(artistName);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void testThatNoArtistAccordingLongIdIsFound() {
        Long amgArtistId = 55555L;
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.empty());
        ResponseEntity<Artist> response = this.controller.getArtist(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testThatFavoriteArtistIsFoundButNoAlbums() {
        Long amgArtistId = 55555L;
        ArtistEntity art1 = new ArtistEntity();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<Album>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void testThatAlbumsAreMappedAndReturned() {
        Long amgArtistId = 55555L;
        ArtistEntity art1 = new ArtistEntity();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        AlbumEntity album1 = new AlbumEntity();
        album1.setCollectionName("Some album");
        album1.setCollectionId(42233434L);
        AlbumEntity album2 = new AlbumEntity();
        album2.setCollectionName("Another album");
        album2.setCollectionId(45454545L);
        art1.setAlbums(Arrays.asList(album1, album2));
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<Album>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).getCollectionId()).isEqualTo(45454545L);
    }

    @Test
    void testThatNoFavoriteArtistIsFoundForRetrievingAlbums() {
        Long amgArtistId = 55555L;
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.empty());
        ResponseEntity<List<Album>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }
}
