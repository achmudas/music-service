package com.music.services;

import com.music.models.internal.Artist;
import com.music.repositories.ArtistRepository;
import com.music.services.integrations.MusicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtistsServiceTests {

    private ArtistsService artistsService;

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private MusicService musicService;

    @BeforeEach
    public void setUp() {
        this.artistsService = new ArtistsService(
                this.artistRepository,
                this.musicService,
                new ModelMapper());
    }

    @Test
    public void testWhenResultIsEmptyCallingItunes() throws Exception{
        String artistName = "non-existing-name";
        when(this.artistRepository.findByArtistNameIgnoreCase(artistName)).thenReturn(Optional.empty());
        when(this.musicService.findArtistsByArtistName(artistName)).thenReturn(Arrays.asList());
        List<Artist> foundArtists = this.artistsService.findArtists(artistName);
        assertThat(foundArtists.isEmpty()).isTrue();
    }

    @Test
    public void testWhenResultIsNullCallingItunes() {

    }


}
