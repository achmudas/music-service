package com.music.api;

import com.music.models.internal.Artist;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Users")
@RequestMapping("artist")
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
    @PostMapping(value="/{userId}/favoriteArtist/{amgArtistId}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Artist> saveFavoriteArtist(
            @ApiParam(value = "User ID", required = true)
            @PathVariable("userId") Long userId,
            @Parameter(description = "AMG Artist id")
            @RequestParam(value="amgArtistId", required = true) Long amgArtistId
    );

}
