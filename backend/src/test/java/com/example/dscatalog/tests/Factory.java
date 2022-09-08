package com.example.dscatalog.tests;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Phone is nice", 800.00,"htttps://img.com", Instant.parse("2020-07-13T20:50:07.12345Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }
    public static ProductDTO createProductDto(){
        Product product = createProduct();
        return new ProductDTO(product,product.getCategories());
    }
}
