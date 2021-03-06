package com.music.models.internal;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity(name = "album")
@Setter
@Getter
@EqualsAndHashCode
public class AlbumEntity {

    @Id
    private Long collectionId;

    @Column(length = 250)
    @NotBlank
    private String collectionName;

    @Column(length = 250)
    @NotBlank
    private String artworkUrl100;

    @Column(length = 250)
    @NotBlank
    private String artistName;

    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Column
    private Long amgArtistId;
}
