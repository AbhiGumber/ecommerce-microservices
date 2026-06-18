package org.ecommerce.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ecommerce.orderservice.dto.CreateOrderRequest;
import org.ecommerce.orderservice.dto.OrderResponse;
import org.ecommerce.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateOrder() throws Exception {

        CreateOrderRequest request =
                new CreateOrderRequest(
                        1L,
                        2
                );

        OrderResponse response =
                new OrderResponse(
                        1L,
                        1L,
                        2,
                        BigDecimal.valueOf(156000),
                        LocalDateTime.now()
                );

        when(orderService.createOrder(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/orders")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                );
    }

    @Test
    void shouldReturnOrderById() throws Exception {

        OrderResponse response =
                new OrderResponse(
                        1L,
                        1L,
                        2,
                        BigDecimal.valueOf(156000),
                        LocalDateTime.now()
                );

        when(orderService.getOrderById(1L))
                .thenReturn(response);

        mockMvc.perform(
                        get("/orders/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                );
    }
    @Test
    void shouldReturnBadRequestForInvalidInput()
            throws Exception {

        CreateOrderRequest request =
                new CreateOrderRequest(
                        1L,
                        0
                );

        mockMvc.perform(
                        post("/orders")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isInternalServerError());
    }
}
