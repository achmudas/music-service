package com.music.repositories;

import com.music.models.internal.Artist;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ArtistRepository extends PagingAndSortingRepository<Artist, Long> {

    Optional<Artist> findByArtistNameIgnoreCase(String artistName);
}
