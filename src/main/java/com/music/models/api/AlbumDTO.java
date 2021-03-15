package com.music.models.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

//#FIXME rename to not DTO
@Setter
@Getter
@ToString
public class AlbumDTO {

    private Long collectionId;
    private String collectionName;
    private String artworkUrl100;
    private String artistName;
    private Date releaseDate;
}
