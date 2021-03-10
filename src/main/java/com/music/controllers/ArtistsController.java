package com.music.controllers;

import com.music.api.ArtistsApi;
import org.springframework.http.ResponseEntity;

public class ArtistsController implements ArtistsApi {

    @Override
    public ResponseEntity<?> getArtists() {
        return null;
    }
}
