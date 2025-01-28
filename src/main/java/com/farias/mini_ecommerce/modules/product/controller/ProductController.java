package com.farias.mini_ecommerce.modules.product.controller;

import com.farias.mini_ecommerce.modules.product.dto.request.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import com.farias.mini_ecommerce.modules.product.service.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/product")
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

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody ProductRequest request) {
        var result = createProductService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/get/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return requested product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "400", description = "Invalid request.")
    })
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        var result = getProductByIdService.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns all products.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "204", description = "Nothing found."),
    })
    public ResponseEntity<Object> getAll(){
        var result = getAllProductsService.execute();

        if(result.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update specified product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "400", description = "Invalid request."),
    })
    public ResponseEntity<Object> update(@Valid @RequestBody ProductRequest request, @PathVariable UUID id) {
        var result = updateProductService.execute(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete specified product.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "400", description = "Invalid request."),
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductService.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
