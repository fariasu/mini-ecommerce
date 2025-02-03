package com.farias.mini_ecommerce.modules.product.controller;

import com.farias.mini_ecommerce.exception.dto.ErrorResponse;
import com.farias.mini_ecommerce.modules.product.dto.request.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductShortResponse;
import com.farias.mini_ecommerce.modules.product.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/product")
@Tag(name = "Product", description = "Browse registered products or, as an administrator, manage the store's products.")
public class ProductController {
    private final CreateProductService createProductService;
    private final GetProductByIdService getProductByIdService;
    private final GetAllProductsService getAllProductsService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;

    public ProductController(CreateProductService createProductService,
                             GetProductByIdService getProductByIdService,
                             GetAllProductsService getAllProductsService,
                             UpdateProductService updateProductService,
                             DeleteProductService deleteProductService) {
        this.createProductService = createProductService;
        this.getProductByIdService = getProductByIdService;
        this.getAllProductsService = getAllProductsService;
        this.updateProductService = updateProductService;
        this.deleteProductService = deleteProductService;
    }

    @PostMapping("/")
    @Operation(
            summary = "Create new product.",
            description = "Endpoint that creates a new product in system.",
            tags = {"Product"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductShortResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> create(@Valid @RequestBody ProductRequest request) {
        var result = createProductService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get product by id.",
            description = "Endpoint that gets the product by id.",
            tags = {"Product"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return requested product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductShortResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        var result = getProductByIdService.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/")
    @Operation(
            summary = "Get all products.",
            description = "Endpoint that gets all products in system.",
            tags = {"Product"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all products.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductShortResponse.class))),
            @ApiResponse(responseCode = "204", description = "Nothing found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        var result = getAllProductsService.execute(pageable);

        if(result.totalProducts() <= 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update existent product by id.",
            description = "Endpoint that update a existent product by id.",
            tags = {"Product"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update specified product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductShortResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Object> update(@Valid @RequestBody ProductRequest request, @PathVariable UUID id) {
        var result = updateProductService.execute(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete existent product by id.",
            description = "Endpoint that deletes existent product by id.",
            tags = {"Product"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete specified product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductShortResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductService.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
