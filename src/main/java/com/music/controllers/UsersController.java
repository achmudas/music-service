package com.music.controllers;

import com.music.api.UsersApi;
import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
import com.music.models.internal.Artist;
import com.music.services.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UsersController implements UsersApi {

    private UsersService usersService;
    private ModelMapper mapper;

    @Autowired
    public UsersController(UsersService usersService,
                           ModelMapper mapper) {
        this.usersService = usersService;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<ArtistDTO> saveFavoriteArtist(Long userId, Long amgArtistId) {
        Optional<Artist> favoritedArtist = this.usersService.saveFavoriteArtist(amgArtistId, userId);
        return (favoritedArtist.isPresent()) ?
                new ResponseEntity<>(mapper.map(favoritedArtist.get(), ArtistDTO.class), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
//        #FIXME need to think about particular error messages
    }

    @Override
    public ResponseEntity<ArtistDTO> getFavoriteArtist(Long userId) {
        Optional<Artist> favoritedArtist = this.usersService.getUsersFavoriteArtist(userId);
        return (favoritedArtist.isPresent()) ?
                new ResponseEntity<>(mapper.map(favoritedArtist.get(), ArtistDTO.class), null, HttpStatus.OK) :
                new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<AlbumDTO>> getFavoriteArtistTopAlbums(Long userId) {
        Optional<Artist> artist = this.usersService.getUsersFavoriteArtist(userId);
        if (artist.isPresent() && !CollectionUtils.isEmpty(artist.get().getAlbums())) {
            List<AlbumDTO> albums = artist.get().getAlbums().stream()
                    .map(album -> mapper.map(album, AlbumDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(albums, null, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), null, HttpStatus.NOT_FOUND);
    }
}
