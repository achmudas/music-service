package com.music.services;

import com.music.models.external.Result;
import com.music.models.internal.Artist;
import com.music.models.internal.User;
import com.music.repositories.ArtistRepository;
import com.music.repositories.UserRepository;
import com.music.services.integrations.MusicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class ArtistsService {

    private ArtistRepository artistRepository;
    private MusicService musicService;
    private ModelMapper mapper;
//    #FIXME move to separate service
    private UserRepository userRepository;

//    #FIXME need to go through all classes and make autowired in constructor

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
     * @param artistName
     * @return List of artists with similar names
     */
//    #FIXME think how to reduce calls to itunes service? Save to database all results from search
//    #FIXME the case when some artists with similar name exists, but there is the one which is actually
//    searched. Need to find exact by name only single in this case?
    public List<Artist> findArtists(String artistName) {
        Optional<Artist> foundArtist = this.artistRepository.findByNameIgnoreCase(artistName);
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
            if (searchedArtist.isEmpty() || searchedArtist.get().getAmgArtistId() == null) {
                return Optional.empty();
            } else {
                return Optional.of(mapAndSaveArtist(searchedArtist.get()));
            }
        }
        return foundArtist;
    }


    private Artist mapAndSaveArtist(Result searchResult) {
        return this.artistRepository.save(
                this.mapper.map(searchResult, Artist.class)
        );
    }

}
