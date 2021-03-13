package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
import com.music.models.internal.Artist;
import com.music.services.ArtistsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
public class ArtistsController implements ArtistsApi {

    private ArtistsService artistsService;
    private ModelMapper mapper;

    @Autowired
    public ArtistsController(ArtistsService artistsService,
                             ModelMapper mapper) {
        this.artistsService = artistsService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<ArtistDTO>> findArtists(String artistName) {
       List<Artist> artists = this.artistsService.findArtists(artistName);

       List<ArtistDTO> artistsDTO = artists.stream()
               .map(artist -> mapper.map(artist, ArtistDTO.class))
               .collect(Collectors.toList());

       return (artistsDTO.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(artistsDTO, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArtistDTO> getArtist(Long amgArtistId) {
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        return foundArtist.isPresent() ?
                new ResponseEntity<>(mapper.map(foundArtist.get(), ArtistDTO.class), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);

    }

    @Override
    public ResponseEntity<List<AlbumDTO>> getFavoriteAlbums(Long amgArtistId) {
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isPresent() && !CollectionUtils.isEmpty(foundArtist.get().getAlbums())) {
            List<AlbumDTO> albums = foundArtist.get().getAlbums().stream()
                    .map(album -> mapper.map(album, AlbumDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(albums, null, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND);
    }
}
