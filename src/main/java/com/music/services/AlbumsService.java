package com.music.services;

import com.music.models.external.Result;
import com.music.models.external.WrapperType;
import com.music.models.internal.Album;
import com.music.services.integrations.MusicService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * Business layer service to retrieve information related to @{@link Album}
 */
@Service
public class AlbumsService {

    private MusicService musicService;
    private ModelMapper modelMapper;

    @Autowired
    public AlbumsService(MusicService musicService,
                         ModelMapper modelMapper) {
        this.musicService = musicService;
        this.modelMapper = modelMapper;
    }


    /**
     * Retrieves albums for provided AMG artist IDs.
     * @param amgArtistIds AMG artists IDs for which albums should be retrieved
     * @return @{@link Map} where key - amgArtistId and value @{@link Set} of @{@link Album}.
     * Set is used to avoid duplicate albums for the same artist.
     */
    public Map<Long, Set<Album>> retrieveAlbums(List<Long> amgArtistIds) {
        List<Result> results = this.musicService.retrieveAlbumsForArtist(amgArtistIds);

        return results.stream()
                .filter(result -> WrapperType.COLLECTION.equals(result.getWrapperType()))
                .map(result -> modelMapper.map(result, Album.class))
                .filter(album -> album.getAmgArtistId() != null)
                .collect(groupingBy(Album::getAmgArtistId, toSet()));
    }
}
