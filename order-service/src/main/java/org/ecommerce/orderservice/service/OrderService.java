package org.ecommerce.orderservice.service;

import org.ecommerce.orderservice.dto.CreateOrderRequest;
import org.ecommerce.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest request);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);
}