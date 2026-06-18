package org.ecommerce.productservice.service;

import lombok.RequiredArgsConstructor;
import org.ecommerce.productservice.dao.ProductRepository;
import org.ecommerce.productservice.dto.CreateProductRequest;
import org.ecommerce.productservice.dto.ProductResponse;
import org.ecommerce.productservice.entity.Product;
import org.ecommerce.productservice.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .quantity(request.quantity())
                .build();
        product = productRepository.save(product);

        return map(product);

    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return map(product);
    }


    private ProductResponse map(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
