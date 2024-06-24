package com.OnlineShopping.Thrilok.ControllerTests;



import com.OnlineShopping.Thrilok.Controller.OrderController;
import com.OnlineShopping.Thrilok.Controller.ProductController;
import com.OnlineShopping.Thrilok.Entity.Order;
import com.OnlineShopping.Thrilok.Entity.Product;

import com.OnlineShopping.Thrilok.Enums.OrderStatus;
import com.OnlineShopping.Thrilok.ServiceLayer.OrderService;
import com.OnlineShopping.Thrilok.ServiceLayer.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }



    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenOrderObject_whenCreateOrder_thenReturnSavedOrder() throws Exception {
        Order order = new Order("1", "2221576", OrderStatus.DELIVERED, 45000, null, null, null, null);
        given(orderService.createOrder(any(Order.class))).willReturn(order);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockMvc.perform(post("/orders/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))

                .andExpect(jsonPath("$.userId").value(order.getUserId()));
    }

    @Test
    public void givenListOfOrders_whenGetAllOrders_thenReturnOrdersList() throws Exception {
        List<Order> listOfOrder = Arrays.asList(
                new Order("1", "2221576", OrderStatus.DELIVERED, 45000, null, null, null, null),
                new Order("2", "2232217", OrderStatus.PENDING, 1500, null, null, null, null)
        );
        given(orderService.getAllOrders()).willReturn(listOfOrder);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockMvc.perform(get("/orders/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listOfOrder.size()));
    }

    @Test
    public void givenOrderId_whenGetOrderById_thenReturnOrderObject() throws Exception {
        String id = "1";
        Order order = new Order(id, "2221576", OrderStatus.DELIVERED, 45000, null, null, null, null);
        given(orderService.getOrderById(id)).willReturn(order);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockMvc.perform(get("/orders/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrderPrice").value(order.getTotalOrderPrice()));
    }




    @Test
    public void givenInvalidOrderId_whenUpdateOrder_thenReturnNotFound() throws Exception {
        String id = "999"; // Assuming this ID does not exist
        Order updatedOrder = new Order(id, "2221576", OrderStatus.DELIVERED, 50000, null, null, null, null);
        when(orderService.getOrderById(id)).thenReturn(null);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockMvc.perform(put("/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrder)))
                .andExpect(status().isNotFound());
    }



    @Test
    public void givenOrderId_whenDeleteOrder_thenReturnNoContent() throws Exception {
        String id = "999"; // Assuming this ID exists for deletion

        // Mocking the service to perform deletion without throwing an exception
        willDoNothing().given(orderService).deleteOrder(id);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        mockMvc.perform(delete("/orders/{id}", id))
                .andExpect(status().isNoContent()); // Expecting 204 No Content
    }

    @Test
    public void whenCreateOrder_thenReturnCreatedOrder() throws Exception {
        // Arrange
        Order order = new Order("1", "2221576", OrderStatus.DELIVERED, 1000, null, null, null, null);
        given(orderService.createOrder(order)).willReturn(order);

        // Act & Assert
        mockMvc.perform(post("/orders/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrderPrice").value(order.getTotalOrderPrice()))
                .andExpect(jsonPath("$.userId").value(order.getUserId()));

    }


}

