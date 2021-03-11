package com.music.services.integrations;

import com.music.models.external.Result;

import java.util.List;
import java.util.Optional;

public interface MusicService {

    /**
     * Connects to music service and searches for possible artists
     * with given artist name
     * @param artistName
     * @return list of possible artists with similar names
     */
    List<Result> findArtistsByArtistName(String artistName);


    Optional<Result> findArtistsByAmgArtistId(Long artistId);

}
