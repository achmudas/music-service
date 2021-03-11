package com.music.models.internal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class User {

    @Id
    private Long id;

    @Column(length = 250, name = "first_name")
    private String firstName;
    @Column(length = 250, name = "last_name")
    private String lastName;

    @ManyToOne
    @JoinColumn(referencedColumnName="amgArtistId")
    private Artist favoriteArtist;

}
