package com.OnlineShopping.Thrilok.RepositoryLayer;

import com.OnlineShopping.Thrilok.Entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
}