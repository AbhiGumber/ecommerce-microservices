package org.ecommerce.orderservice.service;

import org.ecommerce.orderservice.dao.OrdersRepository;
import org.ecommerce.orderservice.dto.CreateOrderRequest;
import org.ecommerce.orderservice.dto.OrderResponse;
import org.ecommerce.orderservice.entity.Order;
import org.ecommerce.orderservice.exception.InsufficientStockException;
import org.ecommerce.orderservice.feigns.NotificationClient;
import org.ecommerce.orderservice.feigns.NotificationRequest;
import org.ecommerce.orderservice.feigns.ProductClient;
import org.ecommerce.orderservice.feigns.ProductResponse;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private ProductClient productClient;
    @Mock
    private OrdersRepository orderRepository;
    @Mock
    private NotificationClient notificationClient;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldCreateOrder() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(
                1L,
                2
        );
        ProductResponse productResponse = new ProductResponse(
                1L,
                "Iphone",
                BigDecimal.valueOf(78000),
                100
        );

        BigDecimal multiply = productResponse.price()
                .multiply(
                        BigDecimal.valueOf(
                                createOrderRequest.quantity()
                        )
                );
        Order orderAfterSaved = Order
                .builder()
                .id(1L)
                .totalPrice(multiply)
                .productId(productResponse.id())
                .build();

        NotificationRequest notificationRequest = new NotificationRequest("1", "Order placed successfully for order id: 1");

        when(productClient.getProductById(1L)).thenReturn(productResponse);
        when(orderRepository.save(any(Order.class))).thenReturn(orderAfterSaved);
        when(notificationClient.sendNotification(notificationRequest)).thenReturn("Notification Sent");

        OrderResponse orderResponse = orderService.createOrder(createOrderRequest);

        assertEquals(1L, orderResponse.id());
        assertEquals(
                multiply,
                orderResponse.totalPrice()
        );
        verify(productClient).getProductById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(notificationClient).sendNotification(notificationRequest);

    }

    @Test
    void shouldThrowExceptionWhenStockInsufficient() {

        CreateOrderRequest request =
                new CreateOrderRequest(
                        1L,
                        10
                );

        ProductResponse product =
                new ProductResponse(
                        1L,
                        "Iphone",
                        BigDecimal.valueOf(78000),
                        5
                );
        when(productClient.getProductById(1L))
                .thenReturn(product);
        assertThrows(
                InsufficientStockException.class,
                () -> orderService.createOrder(request)
        );
        verify(productClient).getProductById(1L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    void shouldReturnOrderById() {
        Order order = Order.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .totalPrice(BigDecimal.valueOf(156000))
                .build();

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(1L);

        assertEquals(1L, response.id());

        verify(orderRepository).findById(1L);
    }

    @Test
    void shouldReturnAllOrders() {

        Order order = Order.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .totalPrice(BigDecimal.valueOf(156000))
                .build();

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        List<OrderResponse> response =
                orderService.getAllOrders();

        assertEquals(1, response.size());

        verify(orderRepository).findAll();
    }
}

