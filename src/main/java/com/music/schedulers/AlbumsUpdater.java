package com.music.schedulers;

import com.music.models.internal.AlbumEntity;
import com.music.models.internal.ArtistEntity;
import com.music.services.AlbumsService;
import com.music.services.ArtistsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class AlbumsUpdater {

    private Logger logger = LoggerFactory.getLogger(AlbumsUpdater.class);

    private ArtistsService artistsService;
    private AlbumsService albumsService;

    private static final int RESULT_SIZE = 10;

    @Autowired
    public AlbumsUpdater(ArtistsService artistsService,
                         AlbumsService albumsService) {
        this.artistsService = artistsService;
        this.albumsService = albumsService;
    }

    /**
     * Service is triggered daily to update albums for each artist in database. <br>
     * To reduce number of calls music service is called in chunks - with 10 artists per one call.
     */
    @PostConstruct
    @Scheduled(cron = "${albums.query.cron}")
    public void updateArtistsAlbums() {
        logger.info("Starting top albums update for artists");
        Page<ArtistEntity> pageableResult = this.artistsService.getAllArtists(0, RESULT_SIZE);

        if (!pageableResult.hasContent()) {
            logger.info("No artists to update albums.");
            return;
        }

        while (true) {
            List<Long> amgArtistIds = getArtistIdsForCheckingAlbums(pageableResult);
            Map<Long, Set<AlbumEntity>> albums = this.albumsService.retrieveAlbums(amgArtistIds);
            updateListOfAlbumsForArtists(albums);
            if (!pageableResult.hasNext()) {
                break;
            }
            pageableResult = getNextPageableResult(pageableResult);
        }
        logger.info("Albums update was completed.");
    }

    private Page<ArtistEntity> getNextPageableResult(Page<ArtistEntity> pageableResult) {
        Pageable pageable = pageableResult.nextPageable();
        pageableResult = this.artistsService.getAllArtists(pageable.getPageNumber(),
                RESULT_SIZE);
        return pageableResult;
    }

    private List<Long> getArtistIdsForCheckingAlbums(Page<ArtistEntity> pageableResult) {
        return pageableResult.getContent().stream()
                .map(ArtistEntity::getAmgArtistId)
                .collect(Collectors.toList());
    }

    private void updateListOfAlbumsForArtists(Map<Long, Set<AlbumEntity>> albums) {
        albums.keySet().stream()
                .map(amgArtistId -> this.artistsService.findArtist(amgArtistId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(artist -> {
                    artist.setAlbums(List.copyOf(albums.get(artist.getAmgArtistId())));
                    this.artistsService.updateArtist(artist);
                });
    }
}
