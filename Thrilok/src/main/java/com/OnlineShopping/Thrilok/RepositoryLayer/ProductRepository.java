package com.OnlineShopping.Thrilok.RepositoryLayer;

import com.OnlineShopping.Thrilok.Entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
