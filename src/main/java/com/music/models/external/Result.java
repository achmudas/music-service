package com.music.models.external;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Result {

        private WrapperType wrapperType;
        private String artistName;
        private Long amgArtistId;
        private CollectionType collectionType;
        private String collectionName;
        private String artworkUrl100;
        private Date releaseDate;
        private Long collectionId;
}
