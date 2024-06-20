package com.OnlineShopping.Thrilok.Entity;

import com.OnlineShopping.Thrilok.Enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Orders")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {
        @Id
        private String id;
        private String userId;
        private OrderStatus orderStatus;
        private double totalOrderPrice;
        private List<Product> products;
        private Date orderDate;


        @CreatedDate
        private LocalDateTime createdAt;

        @LastModifiedDate
        private LocalDateTime updatedAt;

    }



