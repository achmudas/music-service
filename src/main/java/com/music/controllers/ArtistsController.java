package com.music.controllers;

import com.music.api.ArtistsApi;
import com.music.models.external.Result;
import com.music.models.internal.Artist;
import com.music.services.ArtistsService;
import com.music.services.integrations.MusicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ArtistsController implements ArtistsApi {

    @Autowired
    private ArtistsService artistsService;

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

//    #FIXME check if user id is provided
    @Override
    public ResponseEntity<Artist> saveFavoriteArtist(Long userId, Long amgArtistId) {
        Optional<Artist> favoritedArtist = this.artistsService.saveFavoriteArtist(amgArtistId, userId);
        return (favoritedArtist.isPresent()) ?
                new ResponseEntity<>(favoritedArtist.get(), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
//        #FIXME need to think about particular error messages
    }
}
