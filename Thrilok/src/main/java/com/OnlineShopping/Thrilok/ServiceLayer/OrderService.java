package com.OnlineShopping.Thrilok.ServiceLayer;

import com.OnlineShopping.Thrilok.Entity.Order;
import com.OnlineShopping.Thrilok.RepositoryLayer.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        return optionalOrder.orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Order order) {
        Order existingOrder = getOrderById(id);
        existingOrder.setUserId(order.getUserId());
        existingOrder.setOrderStatus(order.getOrderStatus());
        existingOrder.setTotalOrderPrice(order.getTotalOrderPrice());
        existingOrder.setProducts(order.getProducts());
        existingOrder.setOrderDate(order.getOrderDate());
        return orderRepository.save(existingOrder);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}