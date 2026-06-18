package org.ecommerce.notificationservice.record;

public record NotificationRequest(
        String orderId,
        String message
) {
}
