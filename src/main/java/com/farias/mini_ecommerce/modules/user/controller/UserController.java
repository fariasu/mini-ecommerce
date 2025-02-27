package com.farias.mini_ecommerce.modules.user.controller;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.modules.user.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.response.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
@AllArgsConstructor
@Tag(name = "User", description = "Manage users, check login details and register administrators.")
public class UserController {

    private final RegisterUserService registerUserService;
    private final ProfileUserService profileUserService;
    private final UpdateUserService updateUserService;
    private final DeleteUserService deleteUserService;

    @PostMapping("")
    @Operation(
            summary = "Create new user.",
            description = "Endpoint that creates a new user in system.",
            tags = {"User"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email is already registered.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest userRequest) {
        var response = registerUserService.execute(userRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user profile by id.",
            description = "Endpoint that get user profile by id.",
            tags = {"User"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> getProfile(@PathVariable UUID id) {
        var response = profileUserService.execute(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update existent user by id.",
            description = "Endpoint that update a existent user by id.",
            tags = {"User"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "New email already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> update(@PathVariable UUID id, @Valid @RequestBody UserRegisterRequest userRequest) {
        var response = updateUserService.execute(id, userRequest);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete existent user by id.",
            description = "Endpoint that delete a existent user by id.",
            tags = {"User"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "New email already exists.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> delete(@AuthenticationPrincipal String uuid, @PathVariable UUID id) {
        deleteUserService.execute(uuid, id);
        return ResponseEntity.ok().build();
    }

}
