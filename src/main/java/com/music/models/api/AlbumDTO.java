package com.music.models.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AlbumDTO {

    private Long collectionId;
    private String collectionName;
    private String artworkUrl100;
    private String artistName;
    private Date releaseDate;
}
