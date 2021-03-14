package com.music.models.internal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Setter
@Getter
public class Album {

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

    @Transient
    private Long amgArtistId;

}
