package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.internal.Artist;
import com.music.services.ArtistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ArtistsController implements ArtistsApi {

    private ArtistsService artistsService;

    @Autowired
    public ArtistsController(ArtistsService artistsService) {
        this.artistsService = artistsService;
    }


//    @Override
//    public ResponseEntity<List<FoundArtist>> getArtists() {
//        return null;
//    }

    @Override
    public ResponseEntity<List<Artist>> findArtists(String artistName) {
       List<Artist> artists = this.artistsService.findArtists(artistName);
       return (artists == null || artists.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(artists, null, HttpStatus.OK);
    }
}
