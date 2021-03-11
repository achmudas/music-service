package com.music.services;

import com.music.models.internal.Artist;
import com.music.models.internal.User;
import com.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {

    private UserRepository userRepository;
    private ArtistsService artistsService;

    @Autowired
    public UsersService(UserRepository userRepository, ArtistsService artistsService) {
        this.userRepository = userRepository;
        this.artistsService = artistsService;
    }


    public Optional<Artist> saveFavoriteArtist(Long amgArtistId, Long userId) {
        Optional<Artist> foundArtist = this.artistsService.findArtist(amgArtistId);
        if (foundArtist.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> foundUser = this.userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            User user = new User();
            user.setId(userId);
            user.setFavoriteArtist(foundArtist.get());
            this.userRepository.save(user);
        } else {
            foundUser.get().setFavoriteArtist(foundArtist.get());
            this.userRepository.save(foundUser.get());
        }
        return foundArtist;
    }
}
