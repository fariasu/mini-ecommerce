package com.farias.mini_ecommerce.modules.cart.controller;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.service.CartDeleteService;
import com.farias.mini_ecommerce.modules.cart.service.CartGetService;
import com.farias.mini_ecommerce.modules.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
@Tag(name = "Cart", description = "Operations related to the products Cart.")
public class CartController {

    private final CartService cartService;
    private final CartGetService cartGetService;
    private final CartDeleteService cartDeleteService;

    public CartController(
            CartService cartService,
            CartGetService cartGetService,
            CartDeleteService cartDeleteService) {
        this.cartService = cartService;
        this.cartGetService = cartGetService;
        this.cartDeleteService = cartDeleteService;
    }

    @PostMapping("/{productId}")
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
    public ResponseEntity<Object> add(@PathVariable UUID productId, @Valid @RequestBody CartRequest cartRequest, @AuthenticationPrincipal String userId) {
        var result = cartService.execute(productId, cartRequest, userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/get")
    @Operation(
            summary = "Get cart of current logged user.",
            description = "Endpoint that gets the cart of the logged user.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns cart.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> add(@AuthenticationPrincipal String userId) {
        var result = cartGetService.execute(userId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Delete cart of current logged user.",
            description = "Endpoint that deletes the cart of the logged user.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> delete(@AuthenticationPrincipal String userId) {
        cartDeleteService.execute(userId);
        return ResponseEntity.ok().build();
    }
}
