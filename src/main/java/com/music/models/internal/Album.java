package com.music.models.internal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@ToString
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

    @Column
    private Long amgArtistId;

    @ManyToMany(mappedBy = "albums")
    private List<Artist> artists;
}
