package com.music.api;

import com.music.models.api.AlbumDTO;
import com.music.models.api.ArtistDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("artists")
public interface ArtistsApi {

    @Operation(summary = "Find all artists by name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Artists were successfully found",
                    content = {@Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(
                                    schema = @Schema(implementation = ArtistDTO.class))) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "No artists were found according search term",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @GetMapping(value="/{artistName}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ArtistDTO>> findArtists(
            @Parameter(description = "Artist name")
            @RequestParam(value="artistName", required = true) String artistName
    );
    //    #FIXME validate search term

    @Operation(summary = "Find artist by amgArtistId")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Artist was successfully found",
                    content = {@Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ArtistDTO.class)) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artist was not found according id",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @GetMapping(value="/{amgArtistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ArtistDTO> getArtist(
            @Parameter(description = "Artist AMG Id", required = true)
            @PathVariable("amgArtistId") Long amgArtistId
    );

    @Operation(summary = "Get favorite albums for artist")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Favorite albums were successfully retrieved",
                    content = {@Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = AlbumDTO.class))) }),
            @ApiResponse(
                    responseCode = "404",
                    description = "Artist was not found according amgArtistId and/or artist doesn't have any album",
                    content = {@Content() }),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {@Content() })
    })
    @GetMapping(value="/{amgArtistId}/albums", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AlbumDTO>> getFavoriteAlbums(
            @Parameter(description = "Artist AMG Id", required = true)
            @PathVariable("amgArtistId") Long amgArtistId
    );
}
