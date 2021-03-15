package com.music.repositories;

import com.music.models.internal.ArtistEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<ArtistEntity, Long> {

    /**
     * Searches for the artist according name.
     *
     * @param artistName artist name to search
     * @return @{@link java.util.Optional} with possible found @{@link ArtistEntity}
     */
    Optional<ArtistEntity> findByArtistNameIgnoreCase(String artistName);
}
