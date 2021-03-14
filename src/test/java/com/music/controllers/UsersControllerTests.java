package com.music.controllers;

import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
import com.music.models.internal.Album;
import com.music.models.internal.Artist;
import com.music.services.UsersService;
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
class UsersControllerTests {

    private UsersController controller;

    @Mock
    private UsersService usersService;

    @BeforeEach
    public void setUp() {
        this.controller = new UsersController(usersService, new ModelMapper());
    }

    @Test
    void testThatFavoriteArtistIsNotSaved() {
        when(this.usersService.saveFavoriteArtist(55555L, 4444L)).thenReturn(Optional.empty());
        ResponseEntity<ArtistDTO> response = this.controller.saveFavoriteArtist(4444L, 55555L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void testFavoriteArtistIsNotRetrieved() {
        when(this.usersService.getUsersFavoriteArtist(4444L)).thenReturn(Optional.empty());
        ResponseEntity<ArtistDTO> response = this.controller.getFavoriteArtist(4444L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }


    @Test
    void testThatFavoriteArtistForUserIsFoundButNoAlbums() {
        Long userId = 4444L;
        Artist art1 = new Artist();
        art1.setArtistName("Jon Bonjovi");
        art1.setAmgArtistId(55555L);
        when(this.usersService.getUsersFavoriteArtist(userId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteArtistTopAlbums(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

    @Test
    void testThatAlbumsAreMappedAndReturnedForUser() {
        Long userId = 4444L;
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
        when(this.usersService.getUsersFavoriteArtist(userId)).thenReturn(Optional.of(art1));
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteArtistTopAlbums(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).getCollectionId()).isEqualTo(45454545L);
    }

    @Test
    void testThatNoFavoriteArtistForUserIsFoundForRetrievingAlbums() {
        Long userId = 4444L;
        when(this.usersService.getUsersFavoriteArtist(userId)).thenReturn(Optional.empty());
        ResponseEntity<List<AlbumDTO>> response = this.controller.getFavoriteArtistTopAlbums(userId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().isEmpty()).isTrue();
    }

}
