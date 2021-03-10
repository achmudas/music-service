package com.music.api;

import com.music.models.api.FoundArtist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
                    response = FoundArtist.class,
                    responseContainer = "List"),
            @ApiResponse(
                    code = 404,
                    message = "No artists were found according search term",
                    responseContainer = "List"),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error", response = FoundArtist.class)})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<FoundArtist>> findArtists(
            @Parameter(description = "Artist name")
            @RequestParam(value="artistName", required = true) String artistName
    );

//    #FIXME validate search term

}
