package com.music.services;

import com.music.models.internal.ArtistEntity;
import com.music.models.internal.UserEntity;
import com.music.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    private UsersService service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ArtistsService artistsService;

    @Captor
    ArgumentCaptor<UserEntity> userCaptor;

    @BeforeEach
    public void setUp() {
        this.service = new UsersService(userRepository, artistsService);
    }

    @Test
    void testIfArtistToBeFavoritedIsNotFoundInDBItsNotFavorited() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.empty());
        Optional<ArtistEntity> favoritedArtist = this.service.saveFavoriteArtist(amgArtistId, userId);
        verify(this.userRepository, times(0)).save(userCaptor.capture());
        assertThat(favoritedArtist).isNotPresent();
    }

    @Test
    void testIfUserDoesntExistItsCreated() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        ArtistEntity artist = new ArtistEntity();
        artist.setAmgArtistId(amgArtistId);
        artist.setArtistName("Some artist");
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.of(artist));
        when(this.userRepository.findById(userId)).thenReturn(Optional.empty());
        Optional<ArtistEntity> favoritedArtist = this.service.saveFavoriteArtist(amgArtistId, userId);
        verify(this.userRepository, times(1)).save(userCaptor.capture());
        assertThat(favoritedArtist.get().getAmgArtistId()).isEqualTo(amgArtistId);
        assertThat(userCaptor.getValue().getFavoriteArtist().getAmgArtistId()).isEqualTo(amgArtistId);
        assertThat(userCaptor.getValue().getId()).isEqualTo(userId);
    }

    @Test
    void testThatArtistIsFavorited() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        ArtistEntity artist = new ArtistEntity();
        artist.setAmgArtistId(amgArtistId);
        artist.setArtistName("Some artist");
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.of(artist));
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<ArtistEntity> favoritedArtist = this.service.saveFavoriteArtist(amgArtistId, userId);
        verify(this.userRepository, times(1)).save(userCaptor.capture());
        assertThat(favoritedArtist.get().getAmgArtistId()).isEqualTo(amgArtistId);
        assertThat(userCaptor.getValue().getFavoriteArtist().getAmgArtistId()).isEqualTo(amgArtistId);
    }

    @Test
    void testIfUserHasFavoriteArtistItsReturned() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        ArtistEntity artist = new ArtistEntity();
        artist.setAmgArtistId(amgArtistId);
        artist.setArtistName("Some artist");
        user.setFavoriteArtist(artist);
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<ArtistEntity> favoritedArtist = this.service.getUsersFavoriteArtist(userId);
        verify(this.userRepository, times(1)).findById(anyLong());
        assertThat(favoritedArtist.get().getAmgArtistId()).isEqualTo(amgArtistId);
    }

    @Test
    void testIfUserHasNoFavoriteArtistItsNotReturned() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        when(this.userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<ArtistEntity> favoritedArtist = this.service.getUsersFavoriteArtist(userId);
        verify(this.userRepository, times(1)).findById(anyLong());
        assertThat(favoritedArtist).isNotPresent();
    }

    @Test
    void testThatNoFavoriteArtistIsReturned() {
        Long amgArtistId = 55555L;
        Long userId = 2323L;
        when(this.artistsService.findArtist(55555L)).thenReturn(Optional.empty());
        Optional<ArtistEntity> favoritedArtist = this.service.saveFavoriteArtist(amgArtistId, userId);
        assertThat(favoritedArtist).isNotPresent();
        verify(this.userRepository, times(0)).findById(any(Long.class));
    }
}
