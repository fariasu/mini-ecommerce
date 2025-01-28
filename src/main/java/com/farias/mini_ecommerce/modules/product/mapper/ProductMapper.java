package com.farias.mini_ecommerce.modules.product.mapper;

import com.farias.mini_ecommerce.modules.product.entity.Product;
import com.farias.mini_ecommerce.modules.product.dto.request.ProductRequest;
import com.farias.mini_ecommerce.modules.product.dto.response.ProductResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toProduct(ProductRequest productRequest);

    ProductResponse toProductResponse(Product product);

    List<ProductResponse> toProductResponseList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProduct(ProductRequest productRequest, @MappingTarget Product product);
}
