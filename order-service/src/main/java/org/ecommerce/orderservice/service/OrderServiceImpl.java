package org.ecommerce.orderservice.service;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.ecommerce.orderservice.dao.OrdersRepository;
import org.ecommerce.orderservice.dto.CreateOrderRequest;
import org.ecommerce.orderservice.dto.OrderResponse;
import org.ecommerce.orderservice.entity.Order;
import org.ecommerce.orderservice.exception.InsufficientStockException;
import org.ecommerce.orderservice.exception.OrderNotFoundException;
import org.ecommerce.orderservice.exception.ServiceUnavailableException;
import org.ecommerce.orderservice.feigns.NotificationClient;
import org.ecommerce.orderservice.feigns.NotificationRequest;
import org.ecommerce.orderservice.feigns.ProductClient;
import org.ecommerce.orderservice.feigns.ProductResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductClient productClient;
    private final OrdersRepository orderRepository;
    private final NotificationClient notificationClient;

    @Override
    @CircuitBreaker(
            name = "productService",
            fallbackMethod = "productServiceFallback"
    )
    public OrderResponse createOrder(CreateOrderRequest request) {
        ProductResponse product = productClient.getProductById(request.productId());

        if (product.quantity() < request.quantity()) {
            throw new InsufficientStockException(request.quantity());
        }

        BigDecimal totalPrice = product.price()
                .multiply(BigDecimal.valueOf(request.quantity()));
        Order order = Order.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .totalPrice(totalPrice)
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);
        try {
            notificationClient.sendNotification(
                    new NotificationRequest(
                            String.valueOf(order.getId()),
                            "Order placed successfully for order id: " + order.getId()

                    )
            );
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return map(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return map(order);
    }

    private OrderResponse map(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
    public OrderResponse productServiceFallback(
            CreateOrderRequest request,
            Exception ex) {
        if (ex instanceof FeignException.NotFound notFound) {
            throw notFound;
        }
        throw new ServiceUnavailableException("Product Service is currently unavailable");
    }
}
