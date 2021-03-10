package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.FoundArtist;
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
    public ResponseEntity<List<FoundArtist>> findArtists(String artistName) {
        List<FoundArtist> foundArtists = this.musicService.findArtistsByArtistName(artistName);
        return (foundArtists == null || foundArtists.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(foundArtists, null, HttpStatus.OK);
    }
}
