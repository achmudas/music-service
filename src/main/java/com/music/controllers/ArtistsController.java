package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.Album;
import com.music.models.api.Artist;
import com.music.models.internal.ArtistEntity;
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
    public ResponseEntity<List<Artist>> findArtists(String artistName) {
        this.logger.info("Searching for artists according name: {}", artistName);
        List<ArtistEntity> artistEntities = this.artistsService.findArtists(artistName);
        List<Artist> artists = artistEntities.stream()
               .map(artist -> mapper.map(artist, Artist.class))
               .collect(Collectors.toList());
        this.logger.info("Following artists were found: {}", artists);
        return (artists.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(artists, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Artist> getArtist(Long amgArtistId) {
        this.logger.info("Getting artists according amgArtistId: {}", amgArtistId);
        Optional<ArtistEntity> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isPresent()) {
            Artist mappedArtist = mapper.map(foundArtist.get(), Artist.class);
            this.logger.info("Following artist was found: {}", mappedArtist);
            return new ResponseEntity<>(mappedArtist, null, HttpStatus.OK);
        }
        this.logger.info("No artist was found according amgArtistId: {}", amgArtistId);
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Album>> getFavoriteAlbums(Long amgArtistId) {
        this.logger.info("Getting favourite albums for artist: {}", amgArtistId);
        Optional<ArtistEntity> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isPresent() && !CollectionUtils.isEmpty(foundArtist.get().getAlbums())) {
            List<Album> albums = foundArtist.get().getAlbums().stream()
                    .map(album -> mapper.map(album, Album.class))
                    .collect(Collectors.toList());
            this.logger.info("Following albums were found: {}", albums);
            return new ResponseEntity<>(albums, null, HttpStatus.OK);
        }
        this.logger.info("Either artist, amgArtistId: {}, was not found or doesn't have albums", amgArtistId);
        return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND);
    }
}
