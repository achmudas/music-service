package com.music.services;

import com.music.models.internal.Artist;
import com.music.models.internal.User;
import com.music.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Business layer service to retrieve information related to @{@link User}
 */
@Service
public class UsersService {

    private UserRepository userRepository;
    private ArtistsService artistsService;

    @Autowired
    public UsersService(UserRepository userRepository, ArtistsService artistsService) {
        this.userRepository = userRepository;
        this.artistsService = artistsService;
    }

    /**
     * Saves @{@link User} favorite @{@link Artist}. If user doesn't exist yet, it's created.
     * If artist is not found, it's not favorited and empty @{@link java.util.Optional} is returned.
     *
     * @param amgArtistId user's favorite artist's AMG id
     * @param userId - user's ID
     * @return - @{@link java.util.Optional} with user's favorited @{@link Artist}
     */
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

    /**
     * Searches for @{@link User} favorited @{@link Artist}. If none is selected,
     * empty @{@link java.util.Optional} is returned.
     *
     * @param userId user's ID for which favorite artist should be found
     * @return @{@link java.util.Optional} with possible found user's favorite @{@link Artist}
     */
    public Optional<Artist> getUsersFavoriteArtist(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isPresent() && user.get().getFavoriteArtist() != null) {
            return Optional.of(user.get().getFavoriteArtist());
        }
        return Optional.empty();
    }
}
