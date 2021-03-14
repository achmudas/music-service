package com.music.controllers;

import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
import com.music.models.internal.Album;
import com.music.models.internal.Artist;
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
public class ArtistsControllerTests {

    private ArtistsController controller;

    @Mock
    private ArtistsService artistsService;

    @BeforeEach
    public void setUp() {
        this.controller = new ArtistsController(artistsService, new ModelMapper());
    }

    @Test
    void testThatArtistsAreMappedToArtistDTOAndReturned() {
        String artistName = "jon";
        Artist art1 = new Artist();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        Artist art2 = new Artist();
        art2.setArtistName("Jon Something");
        art2.setAmgArtistId(4444L);
        when(this.artistsService.findArtists(artistName)).thenReturn(Arrays.asList(art1, art2));
        ResponseEntity<List<ArtistDTO>> response = this.controller.findArtists(artistName);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).getAmgArtistId()).isEqualTo(4444L);
    }

    @Test
    void testThatNoArtistsAreFound() {
        String artistName = "jon";
        when(this.artistsService.findArtists(artistName)).thenReturn(Arrays.asList());
        ResponseEntity<List<ArtistDTO>> response = this.controller.findArtists(artistName);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void testThatNoArtistAccordingLongIdIsFound() {
        Long amgArtistId = 55555L;
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.empty());
        ResponseEntity<ArtistDTO> response = this.controller.getArtist(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testThatFavoriteArtistIsFoundButNoAlbums() {
        Long amgArtistId = 55555L;
        Artist art1 = new Artist();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void testThatAlbumsAreMappedAndReturned() {
        Long amgArtistId = 55555L;
        Artist art1 = new Artist();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        Album album1 = new Album();
        album1.setCollectionName("Some album");
        album1.setCollectionId(42233434L);
        Album album2 = new Album();
        album2.setCollectionName("Another album");
        album2.setCollectionId(45454545L);
        art1.setAlbums(Arrays.asList(album1, album2));
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).getCollectionId()).isEqualTo(45454545L);
    }

    @Test
    void testThatNoFavoriteArtistIsFoundForRetrievingAlbums() {
        Long amgArtistId = 55555L;
        when(this.artistsService.findArtist(amgArtistId)).thenReturn(Optional.empty());
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteAlbums(amgArtistId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }
}
