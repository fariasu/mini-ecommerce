package com.farias.mini_ecommerce.controller;

import com.farias.mini_ecommerce.dto.request.UserLoginRequest;
import com.farias.mini_ecommerce.dto.request.UserRegisterRequest;
import com.farias.mini_ecommerce.dto.response.UserRegisteredResponse;
import com.farias.mini_ecommerce.service.user.LoginUserService;
import com.farias.mini_ecommerce.service.user.RegisterUserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final RegisterUserService registerUserService;
    private final LoginUserService loginUserService;

    public UserController(RegisterUserService registerUserService,
                          LoginUserService loginUserService) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
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

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        return ResponseEntity.ok().body("FOi");
    }
}
