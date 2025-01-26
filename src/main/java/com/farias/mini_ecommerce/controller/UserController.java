package com.farias.mini_ecommerce.controller;

import com.farias.mini_ecommerce.dto.request.UserRequest;
import com.farias.mini_ecommerce.dto.response.UserResponse;
import com.farias.mini_ecommerce.service.user.CreateUserProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final CreateUserProductService createUserProductService;

    public UserController(CreateUserProductService createUserProductService) {
        this.createUserProductService = createUserProductService;
    }

    @PostMapping("create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> create(@RequestBody UserRequest userRequest) {
        try{
            var response = createUserProductService.execute(userRequest);
            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
