package com.music.models.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
public class Album {

    private Long collectionId;
    private String collectionName;
    private String artworkUrl100;
    private String artistName;
    private Date releaseDate;
}
