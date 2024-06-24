package com.OnlineShopping.Thrilok.ControllerTests;



import com.OnlineShopping.Thrilok.Controller.CartController;
import com.OnlineShopping.Thrilok.Controller.ProductController;
import com.OnlineShopping.Thrilok.Entity.Cart;
import com.OnlineShopping.Thrilok.Entity.Product;

import com.OnlineShopping.Thrilok.ServiceLayer.CartService;
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

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }



    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenCartObject_whenCreateCart_thenReturnSavedCart() throws Exception {
        Cart cart = new Cart("1", "2221576", "Thrilok", "Khammam", 1500, null, null, null);

        given(cartService.createCart(any(Cart.class))).willReturn(cart);

        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(post("/carts/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))

                .andExpect(jsonPath("$.userName").value(cart.getUserName()));
    }

    @Test
    public void givenListOfCarts_whenGetAllCarts_thenReturnCartsList() throws Exception {
        List<Cart> listOfCart = Arrays.asList(
                new Cart("1", "2221576", "Thrilok", "Khammam", 1500, null, null, null),
                new Cart("2", "2232217", "Priya", "Tenali", 10000, null, null, null)
        );
        given(cartService.getAllCarts()).willReturn(listOfCart);

        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(get("/carts/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listOfCart.size()));
    }

    @Test
    public void givenCartId_whenGetCartById_thenReturnCartObject() throws Exception {
        String id = "1";
        Cart cart = new Cart(id, "2221576", "Thrilok", "Khammam", 1500, null, null, null);
        given(cartService.getCartById(id)).willReturn(cart);

        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();

        mockMvc.perform(get("/carts/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(cart.getUserName()));
    }




    @Test
    public void givenInvalidCartId_whenUpdateCart_thenReturnNotFound() throws Exception {
        String id = "999"; // Assuming this ID does not exist
        Cart updatedCart = new Cart(id, "2221576", "Thrilok", "Khammam", 1500, null, null, null);

        // Mocking the service to return null for non-existent cart ID
        when(cartService.getCartById(id)).thenReturn(null);

        mockMvc.perform(put("/carts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCart)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCartId_whenDeleteCart_thenReturnNoContent() throws Exception {
        String id = "999"; // Assuming this ID exists for deletion

        // Mocking the service to perform deletion without throwing an exception
        willDoNothing().given(cartService).deleteCart(id);

        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        mockMvc.perform(delete("/carts/{id}", id))
                .andExpect(status().isNoContent()); // Expecting 204 No Content
    }

    @Test
    public void whenCreateCart_thenReturnCreatedCart() throws Exception {
        // Arrange
        Cart cart = new Cart("1", "2221576", "Thrilok", "Khammam", 1500, null, null, null);
        given(cartService.createCart(cart)).willReturn(cart);

        // Act & Assert
        mockMvc.perform(post("/carts/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(cart.getUserId()))
                .andExpect(jsonPath("$.userName").value(cart.getUserName()))
                .andExpect(jsonPath("$.userAddress").value(cart.getUserAddress()))
                .andExpect(jsonPath("$.totalPrice").value(cart.getTotalPrice()));

    }

}
