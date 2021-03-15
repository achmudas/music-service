package com.music.api;

import com.music.models.api.Album;
import com.music.models.api.Artist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("users")
public interface UsersApi {

    @Operation(summary = "Save artist as a favorite for a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Artist was saved as a favorite",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artist with provided id doesn't exist",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @PostMapping(value="/{userId}/artists/{amgArtistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> saveFavoriteArtist(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId,
            @Parameter(description = "AMG Artist id", required = true)
            @PathVariable(value="amgArtistId", required = true) Long amgArtistId
    );

    @Operation(summary = "Get favorite artist for user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Artist was saved as a favorite",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Artist.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with provided id doesn't exist or it doesn't have favorite artist set",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @GetMapping(value="/{userId}/artists" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> getFavoriteArtist(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId
    );

    @Operation(summary = "Get user's favorite artist's top albums")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Top albums were retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Album.class))) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "User with provided id doesn't exist or it doesn't have favorite artist set",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @GetMapping(value="/{userId}/artists/albums" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Album>> getFavoriteArtistTopAlbums(
            @Parameter(description = "User ID", required = true)
            @PathVariable("userId") Long userId
    );

}
