package com.music.api;

import com.music.models.Artist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "Artist")
@RequestMapping("artists")
public interface ArtistsApi {

    @ApiOperation(value = "Get all artists", nickname = "getArtists", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Artists were successfully retrieved"),
            @ApiResponse(code = 500, message = "Internal server error", response = Artist.class)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getArtists();

}
