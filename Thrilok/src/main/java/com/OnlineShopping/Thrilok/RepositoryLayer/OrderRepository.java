package com.OnlineShopping.Thrilok.RepositoryLayer;

import com.OnlineShopping.Thrilok.Entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}