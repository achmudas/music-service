package com.music.repositories;

import com.music.models.internal.Artist;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<Artist, Long> {

    /**
     * Searches for the artist according name.
     *
     * @param artistName artist name to search
     * @return @{@link java.util.Optional} with possible found @{@link Artist}
     */
    Optional<Artist> findByArtistNameIgnoreCase(String artistName);
}
