package com.music.models.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
public class SearchResponse {

    private List<FoundArtist> results;

}
