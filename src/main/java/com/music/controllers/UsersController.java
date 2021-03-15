package com.music.controllers;

import com.music.api.UsersApi;
import com.music.models.api.Album;
import com.music.models.api.Artist;
import com.music.models.internal.ArtistEntity;
import com.music.services.UsersService;
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
public class UsersController implements UsersApi {

    private Logger logger = LoggerFactory.getLogger(UsersController.class);

    private UsersService usersService;
    private ModelMapper mapper;

    @Autowired
    public UsersController(UsersService usersService,
                           ModelMapper mapper) {
        this.usersService = usersService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<Artist> saveFavoriteArtist(Long userId, Long amgArtistId) {
        this.logger.info("Saving favorite artist: {} for user: {}", amgArtistId, userId);
        Optional<ArtistEntity> favoritedArtist = this.usersService.saveFavoriteArtist(amgArtistId, userId);
        if (favoritedArtist.isPresent()) {
            Artist mappedArtist = mapper.map(favoritedArtist.get(), Artist.class);
            this.logger.info("Following artist was favorited: {} for user: {}", mappedArtist, userId);
            return new ResponseEntity<>(mappedArtist, null, HttpStatus.OK);
        }
        this.logger.info("No artist was favorited for user: {}, amgArtistId: {}", userId, amgArtistId);
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Artist> getFavoriteArtist(Long userId) {
        Optional<ArtistEntity> favoritedArtist = this.usersService.getUsersFavoriteArtist(userId);
        if (favoritedArtist.isPresent()) {
            Artist mappedArtist = mapper.map(favoritedArtist.get(), Artist.class);
            this.logger.info("Following artist {} is favorited for user: {}", mappedArtist, userId);
            return new ResponseEntity<>(mappedArtist, null, HttpStatus.OK);
        }
        this.logger.info("No artist at the moment is favorited for user: {}", userId);
        return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Album>> getFavoriteArtistTopAlbums(Long userId) {
        Optional<ArtistEntity> artist = this.usersService.getUsersFavoriteArtist(userId);
        if (artist.isPresent() && !CollectionUtils.isEmpty(artist.get().getAlbums())) {
            List<Album> albums = artist.get().getAlbums().stream()
                    .map(album -> mapper.map(album, Album.class))
                    .collect(Collectors.toList());
            this.logger.info("Following albums {} are top for users {} favorited artist: {}", albums, userId, artist);
            return new ResponseEntity<>(albums, null, HttpStatus.OK);
        }
        this.logger.info("Either users: {} artist {} is not present or it doesn't have any albums", userId, artist);
        return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND);
    }
}
