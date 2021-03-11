package com.music.models.external;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchResponse {

    private List<Result> results;

}
