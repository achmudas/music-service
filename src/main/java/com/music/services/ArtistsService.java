package com.music.services;

import com.music.models.external.Result;
import com.music.models.internal.ArtistEntity;
import com.music.repositories.ArtistRepository;
import com.music.services.integrations.MusicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business layer service to retrieve information related to @{@link ArtistEntity}
 */
@Service
public class ArtistsService {

    private ArtistRepository artistRepository;
    private MusicService musicService;
    private ModelMapper mapper;

    @Autowired
    public ArtistsService(
            ArtistRepository artistRepository,
            MusicService musicService,
            ModelMapper mapper) {
        this.artistRepository = artistRepository;
        this.musicService = musicService;
        this.mapper = mapper;
    }

    /**
     *
     * Calling music service and returning List of possible artists with similar name. <br>
     * To reduce calls to iTunes service first exact match is checked in DB. <br>
     * Search results also are saved to database for later usage.
     *
     * @param artistName artist name to search for possible @{@link ArtistEntity}
     * @return List of artists with similar names
     */
    public List<ArtistEntity> findArtists(String artistName) {
        Optional<ArtistEntity> foundArtist = this.artistRepository.findByArtistNameIgnoreCase(artistName);
        if (foundArtist.isPresent()) {
            return Arrays.asList(foundArtist.get());
        }

        List<Result> results = this.musicService.findArtistsByArtistName(artistName);
        return results.stream()
                .filter(result -> result.getAmgArtistId() != null)
                .map(result -> mapAndSaveArtist(result))
                .collect(Collectors.toList());
    }

    /**
     * Searches @{@link ArtistEntity} according AMG artist ID. In case in DB is not found @{@link MusicService}
     * is called.
     *
     * @param amgArtistId AMG artist ID to search for @{@link ArtistEntity}
     * @return @{@link java.util.Optional} with possible found @{@link ArtistEntity}
     */
    public Optional<ArtistEntity> findArtist(Long amgArtistId) {
        Optional<ArtistEntity> foundArtist = this.artistRepository.findById(amgArtistId);
        if (foundArtist.isEmpty()) {
            Optional<Result> searchedArtist = this.musicService.findArtistsByAmgArtistId(amgArtistId);
            if (searchedArtist.isEmpty() || searchedArtist.get().getAmgArtistId() == null) {
                return Optional.empty();
            } else {
                return Optional.of(mapAndSaveArtist(searchedArtist.get()));
            }
        }
        return foundArtist;
    }

    /**
     * Finds all artists. Search is executed in chunks.
     * @param page for which exact page search should be executed
     * @param size size of one page results
     * @return @{@link Page} with found @{@link ArtistEntity}
     */
    public Page<ArtistEntity> getAllArtists(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return this.artistRepository.findAll(pageRequest);
    }

    /**
     * Updates @{@link ArtistEntity}
     * @param artist @{@link ArtistEntity} to be saved or updated
     * @return saved @{@link ArtistEntity}
     */
    public ArtistEntity updateArtist(ArtistEntity artist) {
        return this.artistRepository.save(artist);
    }

    private ArtistEntity mapAndSaveArtist(Result searchResult) {
        return this.artistRepository.save(
                this.mapper.map(searchResult, ArtistEntity.class)
        );
    }

}
