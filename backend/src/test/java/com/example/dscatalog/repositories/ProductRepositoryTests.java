package com.example.dscatalog.repositories;

import com.example.dscatalog.entities.Product;
import com.example.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    private long existId;
    private long nonExistId;


    @BeforeEach
     void setUp(){
        existId = 1L;
        nonExistId = 1005L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        productRepository.deleteById(existId);
        Optional<Product> product = productRepository.findById(existId);

        Assertions.assertTrue(product.isEmpty());
    }
    @Test
    public void deleteShouldsThrowsEmptyResultDataAccessExceptionWhereIdDoesNotExist(){

        Assertions.assertThrows(EmptyResultDataAccessException.class,()->{
            productRepository.deleteById(nonExistId);
        });
    }
    @Test
    public void saveShouldsPersistWhereIdIsNullAndAutoIncrement(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());

    }
}
