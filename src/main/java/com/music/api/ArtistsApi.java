package com.music.api;

import com.music.models.internal.Artist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

//    #FIXME expecting swagger doc show more info
//    #FIXME validate search term

    @ApiOperation(value = "Save artist as a favorite", nickname = "saveFavoriteArtist", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Artist was saved as a favorite",
                    response = Artist.class),
            @ApiResponse(
                    code = 404,
                    message = "Artist with provided id doesn't exist"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    #FIXME check how requestheader looks in swagger
//    #FIXME validate x-user-id header?
    ResponseEntity<Artist> saveFavoriteArtist(
            @RequestHeader("x-user-id") Long userId,
            @Parameter(description = "AMG Artist id")
            @RequestParam(value="amgArtistId", required = true) Long amgArtistId
    );

//    #FIXME what when user want's to check his favorite artist?

//    #FIXME endpoint to retrieve top 5 albums for artist

}
