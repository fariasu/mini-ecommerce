package com.farias.mini_ecommerce.modules.cart.controller;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartItemUpdateRequest;
import com.farias.mini_ecommerce.modules.cart.dto.request.CartRequest;
import com.farias.mini_ecommerce.modules.cart.dto.response.CartResponse;
import com.farias.mini_ecommerce.modules.cart.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/cart")
@AllArgsConstructor
@Tag(name = "Cart", description = "Check for registered carts, create one for your user, or checkout.")
@PreAuthorize("hasRole('ROLE_USER')")
public class CartController {

    private final CartService cartService;
    private final CartGetService cartGetService;
    private final CartDeleteService cartDeleteService;
    private final CartItemDeleteService cartItemDeleteService;
    private final CartFinalizeService cartFinalizeService;
    private final CartItemUpdateService cartItemUpdateService;

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

    @GetMapping("/")
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

    @DeleteMapping("/")
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

    @DeleteMapping("/item/{productId}")
    @Operation(
            summary = "Delete cart item of current logged user cart.",
            description = "Endpoint that deletes the cart item of current logged user cart.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart Item deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart Item not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> deleteCartItem(@AuthenticationPrincipal String userId, @PathVariable UUID productId) {
        cartItemDeleteService.execute(userId, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/item/{productId}")
    @Operation(
            summary = "Updates cart item quantity of current logged user cart.",
            description = "Endpoint that updates the cart item quantity of current logged user cart.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart Item deleted successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart or Cart Item not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> updateCartItem(@AuthenticationPrincipal String userId, @PathVariable UUID productId, @Valid @RequestBody CartItemUpdateRequest cartItemUpdateRequest) {
        var result = cartItemUpdateService.execute(userId, productId, cartItemUpdateRequest);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/finalize")
    @Operation(
            summary = "Finalize cart.",
            description = "Endpoint that finalizes the cart.",
            tags = {"Cart"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart finalized successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cart not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Cart has products with insufficient stock.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> finalizeCart(@AuthenticationPrincipal String userId) {
        var response = cartFinalizeService.execute(userId);
        return ResponseEntity.ok().body(response);
    }
}
