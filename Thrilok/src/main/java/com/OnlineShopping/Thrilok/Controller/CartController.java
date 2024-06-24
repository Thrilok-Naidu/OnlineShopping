package com.OnlineShopping.Thrilok.Controller;

import com.OnlineShopping.Thrilok.Entity.Cart;
import com.OnlineShopping.Thrilok.ServiceLayer.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("get/all")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("get/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/cart")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.createCart(cart));
    }
/*
    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody Cart cart) {
        return ResponseEntity.ok(cartService.updateCart(id, cart));
    } */

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable String id, @RequestBody Cart cart) {
        Cart existingCart = cartService.getCartById(id);
        if (existingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Cart updatedCart = cartService.updateCart(id, cart);
        return ResponseEntity.ok(updatedCart);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
