package com.OnlineShopping.Thrilok.ServiceTests;

import com.OnlineShopping.Thrilok.Enums.OrderStatus;
import com.OnlineShopping.Thrilok.ServiceLayer.OrderService;
import com.OnlineShopping.Thrilok.config.jwt.JwtUtils;
import com.OnlineShopping.Thrilok.Entity.Order;
import com.OnlineShopping.Thrilok.RepositoryLayer.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @MockBean
    private JwtUtils jwtUtils;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        order = new Order("1", "2221576", OrderStatus.DELIVERED, 1200, null, null, null, null);
    }

    @Test
    void testGetAllOrder() {
        List<Order> orders= new ArrayList<>();
        orders.add(new Order("1", "2221576", OrderStatus.DELIVERED, 1200, null, null, null, null));
        orders.add(new Order("2", "2232217", OrderStatus.PENDING, 1000, null, null, null, null));

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveOrder() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals(1200, savedOrder.getTotalOrderPrice());
    }





    @Test
    void testGetOrderById() {
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById("1");

        assertEquals(1200, foundOrder.getTotalOrderPrice());
    }


    @Test
    void testUpdateOrder() {
        Order updatedOrder = new Order("1", "2225676", OrderStatus.DELIVERED, 1200, null, null, null, null);

        when(orderRepository.findById("1")).thenReturn(Optional.of(order));
        when(orderRepository.save(updatedOrder)).thenReturn(updatedOrder);

        Order result = orderService.updateOrder("1", updatedOrder);

        assertEquals(OrderStatus.DELIVERED, result.getOrderStatus());
    }



    @Test
    void testDeleteOrder() {
        lenient().when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        orderService.deleteOrder("1");

        verify(orderRepository, times(1)).deleteById("1");
    }


}
