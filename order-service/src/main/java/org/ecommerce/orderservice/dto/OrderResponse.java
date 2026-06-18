package org.ecommerce.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {}