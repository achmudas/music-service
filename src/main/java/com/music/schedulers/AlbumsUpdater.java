package com.music.schedulers;

import com.music.models.internal.Album;
import com.music.models.internal.Artist;
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
        Page<Artist> pageableResult = this.artistsService.getAllArtists(0, RESULT_SIZE);

//        #FIXME need to refactor
        if (!pageableResult.hasContent()) {
            return;
        }

//        #FIXME not really working with Tom Jones case

        while (true) {
            List<Long> amgArtistIds = getArtistIdsForCheckingAlbums(pageableResult);
            Map<Long, List<Album>> albums = this.albumsService.retrieveAlbums(amgArtistIds);
            updateListOfAlbumsForArtists(albums);

            if (!pageableResult.hasNext()) {
                break;
            }
            Pageable pageable = pageableResult.nextPageable();
            pageableResult = this.artistsService.getAllArtists(pageable.getPageNumber(),
                    RESULT_SIZE);
        }
        logger.info("Albums update was completed.");
    }

    private List<Long> getArtistIdsForCheckingAlbums(Page<Artist> pageableResult) {
        List<Long> amgArtistIds = pageableResult.getContent().stream()
                .map(Artist::getAmgArtistId)
                .collect(Collectors.toList());
        return amgArtistIds;
    }

    private void updateListOfAlbumsForArtists(Map<Long, List<Album>> albums) {
        albums.keySet().stream()
                .map(amgArtistId -> this.artistsService.findArtist(amgArtistId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(artist -> {
                    logger.debug("Updating albums for artist: {}", artist.getAmgArtistId());
                    artist.setAlbums(albums.get(artist.getAmgArtistId()));
                    this.artistsService.updateArtist(artist);
                });
    }
}
