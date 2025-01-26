package com.farias.mini_ecommerce.controller.product;

import com.farias.mini_ecommerce.dto.request.ProductRequest;
import com.farias.mini_ecommerce.dto.response.ProductResponse;
import com.farias.mini_ecommerce.service.product.CreateProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private CreateProductService createProductService;

    @PostMapping("/create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponse.class))),
    })
    public ResponseEntity<Object> create(@Valid @RequestBody ProductRequest request) {
        try {
            var result = createProductService.execute(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
