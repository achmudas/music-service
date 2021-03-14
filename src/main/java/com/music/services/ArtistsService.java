package com.music.services;

import com.music.models.external.Result;
import com.music.models.internal.Artist;
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

@Service
public class ArtistsService {

    private ArtistRepository artistRepository;
    private MusicService musicService;
    private ModelMapper mapper;

//    #FIXME add more logging

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
     * @param artistName
     * @return List of artists with similar names
     */
    public List<Artist> findArtists(String artistName) {
        Optional<Artist> foundArtist = this.artistRepository.findByArtistNameIgnoreCase(artistName);
        if (foundArtist.isPresent()) {
            return Arrays.asList(foundArtist.get());
        }

        List<Result> results = this.musicService.findArtistsByArtistName(artistName);
        List<Artist> artists = results.stream()
                .filter(result -> result.getAmgArtistId() != null)
                .map(result -> mapAndSaveArtist(result))
                .collect(Collectors.toList());
        return artists;
    }

    public Optional<Artist> findArtist(Long amgArtistId) {
        Optional<Artist> foundArtist = this.artistRepository.findById(amgArtistId);
        if (foundArtist.isEmpty()) {
            Optional<Result> searchedArtist = this.musicService.findArtistsByAmgArtistId(amgArtistId);
//            #FIXME not all normal artists have amg id, e.g. Bryan Adams
            if (searchedArtist.isEmpty() || searchedArtist.get().getAmgArtistId() == null) {
                return Optional.empty();
            } else {
                return Optional.of(mapAndSaveArtist(searchedArtist.get()));
            }
        }
        return foundArtist;
    }

    public Page<Artist> getAllArtists(int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size);
        return this.artistRepository.findAll(pageRequest);
    }

    public Artist updateArtist(Artist artist) {
        return this.artistRepository.save(artist);
    }


    private Artist mapAndSaveArtist(Result searchResult) {
        return this.artistRepository.save(
                this.mapper.map(searchResult, Artist.class)
        );
    }

}
