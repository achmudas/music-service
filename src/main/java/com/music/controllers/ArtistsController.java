package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.api.Result;
import com.music.models.mongo.Artist;
import com.music.services.MusicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArtistsController implements ArtistsApi {

    @Autowired
    private MusicService musicService;

    @Autowired
    private ModelMapper mapper;

    public ArtistsController(MusicService musicService) {
        this.musicService = musicService;
    }


//    @Override
//    public ResponseEntity<List<FoundArtist>> getArtists() {
//        return null;
//    }

    @Override
    public ResponseEntity<List<Artist>> findArtists(String artistName) {
        List<Result> results = this.musicService.findArtistsByArtistName(artistName);
        List<Artist> artists = results.stream()
                .map(result -> mapper.map(result, Artist.class))
                .collect(Collectors.toList());

        return (artists == null || artists.isEmpty()) ?
                new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(artists, null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Artist> saveFavoriteArtist(String artistId) {
        return null;
    }
}
