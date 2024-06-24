package com.OnlineShopping.Thrilok.ServiceTests;

import com.OnlineShopping.Thrilok.ServiceLayer.CartService;
import com.OnlineShopping.Thrilok.config.jwt.JwtUtils;
import com.OnlineShopping.Thrilok.Entity.Cart;
import com.OnlineShopping.Thrilok.RepositoryLayer.CartRepository;
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
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @MockBean
    private JwtUtils jwtUtils;

    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        cart = new Cart("1", "2221576", "Thrilok", "Khammam", 1500, null, null, null);
    }

    @Test
    void testGetAllCart() {
        List<Cart> carts= new ArrayList<>();
        carts.add(new Cart("1", "2221576", "Thrilok", "Khammam", 1500, null, null, null));
        carts.add(new Cart("2", "2232217", "Priya", "Tenali", 5000, null, null, null));

        when(cartRepository.findAll()).thenReturn(carts);

        List<Cart> result = cartService.getAllCarts();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveCart() {
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart savedCart = cartService.createCart(cart);

        assertNotNull(savedCart);
        assertEquals("Thrilok", savedCart.getUserName());
    }





    @Test
    void testGetCartById() {
        when(cartRepository.findById("1")).thenReturn(Optional.of(cart));

        Cart foundCart = cartService.getCartById("1");

        assertEquals("Thrilok", foundCart.getUserName());
    }


    @Test
    void testUpdateCart() {
        Cart updatedCart = new Cart("1", "2225676", "Vamsi", "Vizag", 10000, null, null, null);

        when(cartRepository.findById("1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(updatedCart)).thenReturn(updatedCart);

        Cart result = cartService.updateCart("1", updatedCart);

        assertEquals("Vamsi", result.getUserName());
    }



    @Test
    void testDeleteCart() {
        lenient().when(cartRepository.findById("1")).thenReturn(Optional.of(cart));

        cartService.deleteCart("1");

        verify(cartRepository, times(1)).deleteById("1");
    }


}
