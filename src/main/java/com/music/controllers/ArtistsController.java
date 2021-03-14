package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
import com.music.models.internal.Artist;
import com.music.services.ArtistsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ArtistsController implements ArtistsApi {

    private Logger logger = LoggerFactory.getLogger(ArtistsController.class);

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
        this.logger.info("Searching for artists according name: {}", artistName);
        List<Artist> artists = this.artistsService.findArtists(artistName);
        List<ArtistDTO> artistsDTO = artists.stream()
               .map(artist -> mapper.map(artist, ArtistDTO.class))
               .collect(Collectors.toList());
        this.logger.info("Following artists were found: {}", artistsDTO);
        return (artistsDTO.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(artistsDTO, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArtistDTO> getArtist(Long amgArtistId) {
        this.logger.info("Getting artists according amgArtistId: {}", amgArtistId);
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isPresent()) {
            ArtistDTO mappedArtist = mapper.map(foundArtist.get(), ArtistDTO.class);
            this.logger.info("Following artist was found: {}", mappedArtist);
            return new ResponseEntity<>(mappedArtist, null, HttpStatus.OK);
        }
        this.logger.info("No artist was found according amgArtistId: {}", amgArtistId);
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<AlbumDTO>> getFavoriteAlbums(Long amgArtistId) {
        this.logger.info("Getting favourite albums for artist: {}", amgArtistId);
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isPresent() && !CollectionUtils.isEmpty(foundArtist.get().getAlbums())) {
            List<AlbumDTO> albums = foundArtist.get().getAlbums().stream()
                    .map(album -> mapper.map(album, AlbumDTO.class))
                    .collect(Collectors.toList());
            this.logger.info("Following albums were found: {}", albums);
            return new ResponseEntity<>(albums, null, HttpStatus.OK);
        }
        this.logger.info("Either artist, amgArtistId: {}, was not found or doesn't have albums", amgArtistId);
        return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND);
    }
}
