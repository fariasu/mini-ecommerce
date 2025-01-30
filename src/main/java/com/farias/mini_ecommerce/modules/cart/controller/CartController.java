package com.farias.mini_ecommerce.modules.cart.controller;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cart")
@Tag(name = "Product", description = "Operations related to the products Cart.")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/products")
    @Operation(
            summary = "Add a Product to Cart. If Cart does not exists, create a new Cart and add the Product.",
            description = "Endpoint that adds a Product to cart and if the Cart does not exists create a new one and add the Product.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Unavailable or not found..", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> add(@Valid @RequestBody CartRequest cartRequest, @AuthenticationPrincipal String userId) {
        var result = cartService.execute(cartRequest, userId);
        return ResponseEntity.ok(result);
    }
}
