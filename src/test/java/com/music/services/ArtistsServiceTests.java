package com.music.services;

import com.music.models.external.Result;
import com.music.models.external.WrapperType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistsServiceTests {

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
    void testWhenNoResultIsCalledEmptyListIsReturned() {
        String artistName = "non-existing-name";
        when(this.artistRepository.findByArtistNameIgnoreCase(artistName)).thenReturn(Optional.empty());
        when(this.musicService.findArtistsByArtistName(artistName)).thenReturn(Arrays.asList());
        List<Artist> foundArtists = this.artistsService.findArtists(artistName);
        assertThat(foundArtists.isEmpty()).isTrue();
    }

    @Test
    void testWhenResultIsFilteredIfNoAmgId() {
        String artistName = "non-existing-name";
        when(this.artistRepository.findByArtistNameIgnoreCase(artistName)).thenReturn(Optional.empty());
        when(this.artistRepository.save(any(Artist.class))).then(invocation ->
                invocation.getArguments()[0]);
        Result res1 = new Result();
        res1.setArtistName("non-existing-name");
        res1.setWrapperType(WrapperType.ARTIST);

        Result res2 = new Result();
        res2.setArtistName("non-existing-name2");
        res2.setAmgArtistId(3734L);
        res2.setWrapperType(WrapperType.ARTIST);
        when(this.musicService.findArtistsByArtistName(artistName)).thenReturn(Arrays.asList(res1, res2));
        List<Artist> foundArtists = this.artistsService.findArtists(artistName);
        assertThat(foundArtists.size()).isOne();
        assertThat(foundArtists.get(0).getAmgArtistId()).isEqualTo(3734L);
    }

    @Test
    void testThatItunesIsCalledIfArtistNotFoundById() {
        Long amgArtistId = 55555L;
        Result res1 = new Result();
        res1.setArtistName("non-existing-name2");
        res1.setAmgArtistId(55555L);
        res1.setWrapperType(WrapperType.ARTIST);

        when(this.artistRepository.findById(amgArtistId)).thenReturn(Optional.empty());
        when(this.musicService.findArtistsByAmgArtistId(amgArtistId)).thenReturn(Optional.of(res1));
        when(this.artistRepository.save(any(Artist.class))).then(invocation ->
                invocation.getArguments()[0]);
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        assertThat(foundArtist.get().getAmgArtistId()).isEqualTo(55555L);
        verify(this.musicService, times(1)).findArtistsByAmgArtistId(any(Long.class));
    }

    @Test
    void testThatItunesIsCalledButNothingIsFound() {
        Long amgArtistId = 55555L;
        Result res1 = new Result();
        res1.setArtistName("non-existing-name2");
        res1.setAmgArtistId(55555L);
        res1.setWrapperType(WrapperType.ARTIST);

        when(this.artistRepository.findById(amgArtistId)).thenReturn(Optional.empty());
        when(this.musicService.findArtistsByAmgArtistId(amgArtistId)).thenReturn(Optional.empty());
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        assertThat(foundArtist).isNotPresent();
        verify(this.musicService, times(1)).findArtistsByAmgArtistId(any(Long.class));
    }
}
