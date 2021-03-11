package com.music.api;

import com.music.models.api.Result;
import com.music.models.mongo.Artist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api(value = "Artist")
@RequestMapping("artist")
public interface ArtistsApi {

//    @ApiOperation(value = "Get all artists", nickname = "getArtists", notes = "", tags = {
//            "Artists"})
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Artists were successfully retrieved",
//                    response = FoundArtist.class,
//                    responseContainer = "List"),
//            @ApiResponse(
//                    code = 500,
//                    message = "Internal server error")})
////    #FIXME provide error response class
////                    response = ErrorResponse.class)})
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<List<FoundArtist>> getArtists();

    @ApiOperation(value = "Find all artists by name", nickname = "getArtists", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Artists were successfully found",
                    response = Artist.class,
                    responseContainer = "List"),
            @ApiResponse(
                    code = 404,
                    message = "No artists were found according search term",
                    responseContainer = "List"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Artist>> findArtists(
            @Parameter(description = "Artist name")
            @RequestParam(value="artistName", required = true) String artistName
    );

//    #FIXME validate search term

    @ApiOperation(value = "Save artist as a favorite", nickname = "saveFavoriteArtist", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Artist was saved as a favorite",
                    response = Artist.class),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> saveFavoriteArtist(
            @Parameter(description = "Artist id")
            @RequestParam(value="artistId", required = true) String artistId
    );

}
