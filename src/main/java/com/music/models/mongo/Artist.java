package com.music.models.mongo;

import com.music.models.api.WrapperType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Artist {

    private String name;
    private String id;
    private WrapperType wrapperType;

}
