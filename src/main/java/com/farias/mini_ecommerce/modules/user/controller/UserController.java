package com.farias.mini_ecommerce.modules.user.controller;

import com.farias.mini_ecommerce.modules.user.dto.UserLoginRequest;
import com.farias.mini_ecommerce.modules.user.dto.UserProfileResponse;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisterRequest;
import com.farias.mini_ecommerce.modules.user.dto.UserRegisteredResponse;
import com.farias.mini_ecommerce.modules.user.service.LoginUserService;
import com.farias.mini_ecommerce.modules.user.service.ProfileUserService;
import com.farias.mini_ecommerce.modules.user.service.RegisterUserService;
import com.farias.mini_ecommerce.modules.user.service.UpdateUserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final RegisterUserService registerUserService;
    private final LoginUserService loginUserService;
    private final ProfileUserService profileUserService;
    private final UpdateUserService updateUserService;

    public UserController(
            RegisterUserService registerUserService,
            LoginUserService loginUserService,
            ProfileUserService profileUserService,
            UpdateUserService updateUserService) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
        this.profileUserService = profileUserService;
        this.updateUserService = updateUserService;
    }

    @PostMapping("/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegisterRequest userRequest) {
        try{
            var response = registerUserService.execute(userRequest);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> login(@Valid @RequestBody UserLoginRequest userRequest) {
        try {
            var response = loginUserService.execute(userRequest);
            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> getProfile(@PathVariable UUID id) {
        try{
            var response = profileUserService.execute(id);
            return ResponseEntity.ok().body(response);

        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/profile/update/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserRegisteredResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> update(@PathVariable UUID id, @Valid @RequestBody UserRegisterRequest userRequest) {
        try{
            var response = updateUserService.execute(id, userRequest);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
