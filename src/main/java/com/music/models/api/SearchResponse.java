package com.music.models.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchResponse {

    private List<Result> results;

}
