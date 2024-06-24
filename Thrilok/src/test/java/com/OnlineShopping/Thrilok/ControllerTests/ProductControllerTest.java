package com.OnlineShopping.Thrilok.ControllerTests;



import com.OnlineShopping.Thrilok.Controller.ProductController;
import com.OnlineShopping.Thrilok.Entity.Product;

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
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;




@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }



    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void givenProductObject_whenCreateProduct_thenReturnSavedProduct() throws Exception {
        Product product = new Product("1", "laptop", "for working purpose", 45000, "electronics", 1000, null, null);
        given(productService.createProduct(any(Product.class))).willReturn(product);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(post("/products/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))

                .andExpect(jsonPath("$.productName").value(product.getProductName()));
    }

    @Test
    public void givenListOfProducts_whenGetAllProducts_thenReturnProductsList() throws Exception {
        List<Product> listOfProduct = Arrays.asList(
                new Product("1", "laptop", "for working purpose", 45000, "electronics", 1000, null, null),
                new Product("2", "fan", "for cooling purpose", 1500, "home needs", 150, null, null)
        );
        given(productService.getAllProducts()).willReturn(listOfProduct);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/products/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(listOfProduct.size()));
    }

    @Test
    public void givenProductId_whenGetProductById_thenReturnProductObject() throws Exception {
        String id = "1";
        Product product = new Product(id, "laptop", "for work purpose", 45000, "electronics", 1000, null, null);
        given(productService.getProductById(id)).willReturn(product);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/products/get/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(product.getProductName()));
    }


/*
    @Test
    @WithMockUser(username ="admin",authorities = {"admin"})
    public void givenUpdatedProduct_whenUpdateProduct_thenReturnUpdatedProductObject() throws Exception {
        String Id = "1";
        Product updatedProduct= new Product(Id, "laptop", "for working purpose", 50000, "electronics", 1000, null, null);

        // Use lenient stubbing instead of strict stubbing
        given(productService.getProductById(Id)).willReturn(updatedProduct);

        // Rest of your test logic
        given(productService.updateProduct(anyString(), any(Product.class))).willReturn(updatedProduct);


        ResultActions response = mockMvc.perform(put("/products/p/{id}", Id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));


        response.andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.userId", is(updatedProduct.getProductPrice())));
    }    */

    @Test
    public void givenInvalidProductId_whenUpdateProduct_thenReturnNotFound() throws Exception {
        String id = "999"; // Assuming this ID does not exist
        Product updatedProduct = new Product(id, "laptop", "for working purpose", 50000, "electronics", 1000, null, null);
        lenient().when(productService.getProductById(id)).thenReturn(null);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(put("/products/p/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());
    }

   /* @Test
    public void givenProductId_whenDeleteProduct_thenReturnNoContent() throws Exception {
        String id = "1";
        doNothing().when(productService).deleteProduct(id);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(delete("/products/p/{id}", id))
                .andExpect(status().isNoContent());
    }  */

    @Test
    public void givenProductId_whenDeleteProduct_thenReturnNoContent() throws Exception {
        String id = "999"; // Assuming this ID exists for deletion

        // Mocking the service to perform deletion without throwing an exception
        willDoNothing().given(productService).deleteProduct(id);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNoContent()); // Expecting 204 No Content
    }

    @Test
    public void whenCreateProduct_thenReturnCreatedProduct() throws Exception {
        // Arrange
        Product product = new Product("1", "Laptop", "Description", 1000, "Electronics", 100, null, null);
        given(productService.createProduct(product)).willReturn(product);

        // Act & Assert
        mockMvc.perform(post("/products/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.productDescription").value(product.getProductDescription()))
                .andExpect(jsonPath("$.productPrice").value(product.getProductPrice()))
                .andExpect(jsonPath("$.productCategory").value(product.getProductCategory()))
                .andExpect(jsonPath("$.productStock").value(product.getProductStock()));
    }

    @Test
    public void givenPageRequest_whenGetProductsByPage_thenReturnPagedProducts() throws Exception {
        // Arrange
        int page = 0;
        int size = 5;
        String sortBy = "productName";
        String sortDir = "asc";
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Product product1 = new Product("1", "Laptop", "Description", 1000, "Electronics", 100, null, null);
        Product product2 = new Product("2", "Phone", "Description", 500, "Electronics", 50, null, null);
        Page<Product> pagedProducts = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);

        given(productService.getProductsByPage(pageable)).willReturn(pagedProducts);

        // Act & Assert
        mockMvc.perform(get("/products/get/page")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("sortDir", sortDir))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId()))
                .andExpect(jsonPath("$.content[0].productName").value(product1.getProductName()))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId()))
                .andExpect(jsonPath("$.content[1].productName").value(product2.getProductName()));
    }

}
