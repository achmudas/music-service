package com.music.services.integrations;

import com.music.models.external.Result;

import java.util.List;
import java.util.Optional;

/**
 * Music service interface to integrate with 3rd party services in order to
 * search and retrieve artists by name or by id. Also to retrieve the list
 * of albums the artist has created.
 */
public interface MusicService {

    /**
     * Connects to music service and searches for possible artists
     * with given artist name
     * @param artistName artist name, searches only acording alphanumeric and space characters
     * @return list of possible artists with similar names
     * @throws com.music.exceptions.GeneralResponseException in case of error from 3rd party service
     */
    List<Result> findArtistsByArtistName(String artistName);

    /**
     * Searches for artist according AMG artist id.
     * @param amgArtistId AMG artist ID to search
     * @return artist if found according ID. Otherwise will be empty @{@link java.util.Optional}
     * @throws com.music.exceptions.GeneralResponseException in case of error from 3rd party service
     */
    Optional<Result> findArtistsByAmgArtistId(Long amgArtistId);

    /**
     * Retrieves top 5 albums for each passed in AMG artist ID.
     *
     * @param amgArtistIds AMG artists IDs for which albums should be retrieved
     * @return @{@link List} of albums along with each artist
     * @throws com.music.exceptions.GeneralResponseException in case of error from 3rd party service
     */
    List<Result> retrieveAlbumsForArtist(List<Long> amgArtistIds);

}
