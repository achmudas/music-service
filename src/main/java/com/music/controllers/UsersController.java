package com.music.controllers;

import com.music.api.UsersApi;
import com.music.models.internal.Artist;
import com.music.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UsersController implements UsersApi {

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public ResponseEntity<Artist> saveFavoriteArtist(Long userId, Long amgArtistId) {
        //    #FIXME check if user id is provided
        Optional<Artist> favoritedArtist = this.usersService.saveFavoriteArtist(amgArtistId, userId);
        return (favoritedArtist.isPresent()) ?
                new ResponseEntity<>(favoritedArtist.get(), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
//        #FIXME need to think about particular error messages
    }

    @Override
    public ResponseEntity<Artist> getFavoriteArtist(Long userId) {
        Optional<Artist> favoritedArtist = this.usersService.getUsersFavoriteArtist(userId);
        return (favoritedArtist.isPresent()) ?
                new ResponseEntity<>(favoritedArtist.get(), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Artist> getFavoriteArtistTopAlbums(Long userId) {
        return null;
    }
}
