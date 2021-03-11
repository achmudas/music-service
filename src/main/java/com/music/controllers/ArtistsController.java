package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.Result;
import com.music.services.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArtistsController implements ArtistsApi {

    @Autowired
    private MusicService musicService;

    public ArtistsController(MusicService musicService) {
        this.musicService = musicService;
    }


//    @Override
//    public ResponseEntity<List<FoundArtist>> getArtists() {
//        return null;
//    }

    @Override
    public ResponseEntity<List<Result>> findArtists(String artistName) {
        List<Result> results = this.musicService.findArtistsByArtistName(artistName);
        return (results == null || results.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(results, null, HttpStatus.OK);
    }
}
