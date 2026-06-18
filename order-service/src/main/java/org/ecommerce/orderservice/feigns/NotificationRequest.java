package org.ecommerce.orderservice.feigns;

public record NotificationRequest(
        String orderId,
        String message
) {
}
