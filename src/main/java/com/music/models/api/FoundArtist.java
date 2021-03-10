package com.music.models.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class FoundArtist {

        private WrapperType wrapperType;
        private String artistName;
        private String artistId;

}
