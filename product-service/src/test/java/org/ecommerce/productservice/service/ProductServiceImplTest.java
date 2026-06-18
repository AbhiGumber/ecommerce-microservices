package org.ecommerce.productservice.service;

import org.ecommerce.productservice.dao.ProductRepository;
import org.ecommerce.productservice.dto.CreateProductRequest;
import org.ecommerce.productservice.dto.ProductResponse;
import org.ecommerce.productservice.entity.Product;
import org.ecommerce.productservice.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void shouldCreateProduct() {
        CreateProductRequest createProductRequest = new CreateProductRequest(
                "Iphone",
                BigDecimal.valueOf(78000),
                10
        );

        Product productAfterSaved = Product
                .builder()
                .id(1L)
                .quantity(10)
                .name("Iphone")
                .build();

        when(productRepository.save(any(Product.class)))
                .thenReturn(productAfterSaved);

        ProductResponse response = productService.createProduct(createProductRequest);

        assertEquals(1L, response.id());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldReturnProductById() {
        Product product = Product
                .builder()
                .id(1L)
                .quantity(10)
                .name("Iphone")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response=productService.getProductById(1L);
        assertEquals(1L, response.id());

        verify(productRepository).findById(any(Long.class));

    }
    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(1L)
        );
    }
    @Test
    void shouldReturnAllProducts() {
        Product product = Product.builder()
                .id(1L)
                .name("Iphone")
                .price(BigDecimal.valueOf(78000))
                .quantity(10)
                .build();

        when(productRepository.findAll())
                .thenReturn(List.of(product));
        List<ProductResponse> response = productService.getAllProducts();
        assertEquals(1, response.size());
        assertEquals("Iphone", response.getFirst().name());

        verify(productRepository).findAll();
    }

}
