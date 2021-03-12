package com.music.api;

import com.music.models.internal.Artist;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "Users")
@RequestMapping("users")
public interface UsersApi {

    @ApiOperation(value = "Save artist as a favorite for a user", nickname = "saveFavoriteArtist", notes = "", tags = {
            "Users"})
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
    @PostMapping(value="/{userId}/artists/{amgArtistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> saveFavoriteArtist(
            @ApiParam(value = "User ID", required = true)
            @PathVariable("userId") Long userId,
            @ApiParam(value = "AMG Artist id", required = true)
            @PathVariable(value="amgArtistId", required = true) Long amgArtistId
    );

    @ApiOperation(value = "Get favorite artist for user", nickname = "getFavoriteArtist", notes = "", tags = {
            "Users"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Favorite artist was retrieved",
                    response = Artist.class),
            @ApiResponse(
                    code = 404,
                    message = "User with provided id doesn't exist or it doesn't have favorite artist set"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @GetMapping(value="/{userId}/artists" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> getFavoriteArtist(
            @ApiParam(value = "User ID", required = true)
            @PathVariable("userId") Long userId
    );

    @ApiOperation(value = "Get user's favorite artist's top albums", nickname = "getFavoriteArtistTopAlbums", notes = "", tags = {
            "Users"})
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Top albums were retrieved",
                    response = Artist.class),
            @ApiResponse(
                    code = 404,
                    message = "User with provided id doesn't exist or it doesn't have favorite artist set"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error")})
    @GetMapping(value="/{userId}/artists/albums" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> getFavoriteArtistTopAlbums(
            @ApiParam(value = "User ID", required = true)
            @PathVariable("userId") Long userId
    );

}
