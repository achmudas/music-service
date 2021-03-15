package com.music.models.internal;

import com.music.models.external.WrapperType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Setter
@Getter
public class Artist {

    @Id
    private Long amgArtistId;

    @Column(length = 250)
    @NotBlank
    private String artistName;

    @Enumerated(EnumType.STRING)
    private WrapperType wrapperType;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Album> albums;

}
