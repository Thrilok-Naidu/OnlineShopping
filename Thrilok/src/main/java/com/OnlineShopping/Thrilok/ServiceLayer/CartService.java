package com.OnlineShopping.Thrilok.ServiceLayer;

import com.OnlineShopping.Thrilok.Entity.Cart;
import com.OnlineShopping.Thrilok.RepositoryLayer.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Cart getCartById(String id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        return optionalCart.orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart updateCart(String id, Cart cart) {
        Cart existingCart = getCartById(id);
        existingCart.setUserId(cart.getUserId());
        existingCart.setUserName(cart.getUserName());
        existingCart.setUserAddress(cart.getUserAddress());
        existingCart.setTotalPrice(cart.getTotalPrice());
        existingCart.setProducts(cart.getProducts());
        return cartRepository.save(existingCart);
    }

    public void deleteCart(String id) {
        cartRepository.deleteById(id);
    }
}