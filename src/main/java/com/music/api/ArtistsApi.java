package com.music.api;

import com.music.models.internal.Album;
import com.music.models.internal.Artist;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Artist")
@RequestMapping("artists")
public interface ArtistsApi {

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
    @GetMapping(value="/{artistName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Artist>> findArtists(
            @Parameter(description = "Artist name")
            @RequestParam(value="artistName", required = true) String artistName
    );
    //    #FIXME validate search term

    @ApiOperation(value = "Find artist by amgArtistId", nickname = "getArtist", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Artist was successfully retrieved",
                    response = Artist.class),
            @ApiResponse(
                    code = 404,
                    message = "Artists was not found according amgArtistId",
                    responseContainer = "List"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @GetMapping(value="/{amgArtistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Artist>> getArtist(
            @ApiParam(value = "Artist AMG Id", required = true)
            @PathVariable("amgArtistId") Long amgArtistId
    );

    @ApiOperation(value = "Get favorite albums for artist", nickname = "getFavoriteAlbums", notes = "", tags = {
            "Artists"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Favorite albums were successfully retrieved",
                    response = Album.class,
                    responseContainer = "List"),
            @ApiResponse(
                    code = 404,
                    message = "Artist was not found according amgArtistId and/or artist doesn't have any album",
                    responseContainer = "List"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @GetMapping(value="/{amgArtistId}/albums", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Album>> getFavoriteAlbums(
            @ApiParam(value = "Artist AMG Id", required = true)
            @PathVariable("amgArtistId") Long amgArtistId
    );

//    #FIXME expecting swagger doc show more info

//    #FIXME do we really need specific endpoints for albums?
//    probably will need to map to specific entities for API
}
