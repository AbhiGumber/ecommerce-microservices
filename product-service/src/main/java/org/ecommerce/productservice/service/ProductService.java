package org.ecommerce.productservice.service;

import org.ecommerce.productservice.dto.CreateProductRequest;
import org.ecommerce.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(CreateProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);
}
