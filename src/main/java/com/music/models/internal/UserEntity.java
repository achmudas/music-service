package com.music.models.internal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "user")
@Setter
@Getter
public class UserEntity {

    @Id
    private Long id;

    @Column(length = 250, name = "first_name")
    private String firstName;
    @Column(length = 250, name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(referencedColumnName="amgArtistId")
    private ArtistEntity favoriteArtist;

}
