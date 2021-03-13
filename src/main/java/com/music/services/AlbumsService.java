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
import java.util.stream.Collectors;

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

    public Map<Long, List<Album>> retrieveAlbums(List<Long> amgArtistIds) {
        List<Result> results = this.musicService.retrieveAlbumsForArtist(amgArtistIds);

        return results.stream()
                .filter(result -> WrapperType.COLLECTION.equals(result.getWrapperType()))
                .map(result -> modelMapper.map(result, Album.class))
                .filter(album -> album.getAmgArtistId() != null)
                .collect(Collectors.groupingBy(Album::getAmgArtistId));
    }
}
