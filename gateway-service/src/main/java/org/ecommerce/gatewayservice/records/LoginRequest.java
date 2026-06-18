package org.ecommerce.gatewayservice.records;

public record LoginRequest(
        String username,
        String password
) {
}
