package com.OnlineShopping.Thrilok.ServiceTests;

import com.OnlineShopping.Thrilok.RepositoryLayer.ProductRepository;
import com.OnlineShopping.Thrilok.ServiceLayer.ProductService;
import com.OnlineShopping.Thrilok.config.jwt.JwtUtils;
import com.OnlineShopping.Thrilok.Entity.Product;
import com.OnlineShopping.Thrilok.RepositoryLayer.ProductRepository;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @MockBean
    private JwtUtils jwtUtils;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        product = new Product("1", "Laptop", "Working Purpose", 45000, "Electronics", 100, null, null);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products= new ArrayList<>();
        products.add(new Product("1", "Laptop", "Working Purpose", 450000, "Electronics", 100, null, null));
        products.add(new Product("2", "fan", "for cooling purpose", 2000, "Interior", 200, null, null));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertNotNull(savedProduct);
        assertEquals("Laptop", savedProduct.getProductName());
    }





    @Test
    void testGetProductById() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById("1");

        assertEquals("Laptop", foundProduct.getProductName());
    }


    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product("1", "watch", "for time", 1500, "smartwatch", 10, null, null);

        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct("1", updatedProduct);

        assertEquals("watch", result.getProductName());
    }



    @Test
    void testDeleteProduct() {
        lenient().when(productRepository.findById("1")).thenReturn(Optional.of(product));

        productService.deleteProduct("1");

        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    public void testGetProductsByPage() {
        // Mock data
        Product product1 = new Product("1", "Product 1", "Description 1", 10.0, "Category A", 100, null, null);
        Product product2 = new Product("2", "Product 2", "Description 2", 20.0, "Category B", 150, null, null);
        List<Product> productList = Arrays.asList(product1, product2);

        // Mocking repository behavior
        Pageable pageable = PageRequest.of(0, 10, Sort.by("productName").ascending());
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());
        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // Call service method
        Page<Product> result = productService.getProductsByPage(pageable);

        // Assert the result
        assertEquals(2, result.getTotalElements()); // Assert total elements
        assertEquals(product1, result.getContent().get(0)); // Assert first product in page content
        assertEquals(product2, result.getContent().get(1)); // Assert second product in page content
    }
}



