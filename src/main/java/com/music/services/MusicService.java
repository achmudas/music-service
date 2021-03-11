package com.music.services;

import com.music.models.api.Result;

import java.util.List;

public interface MusicService {

    /**
     * Connects to music service and searches for possible artists
     * with given artist name
     * @param artistName
     * @return
     */
    List<Result> findArtistsByArtistName(String artistName);

}
