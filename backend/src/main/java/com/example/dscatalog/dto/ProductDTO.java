package com.example.dscatalog.dto;

import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;

import javax.persistence.Column;
import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Double price;
    private String description;
    private String img_url;
    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(){
    }

    public ProductDTO(Long id, String name, Double price, String description, String img_url, Instant date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.img_url = img_url;
        this.date = date;
    }
    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.description = entity.getDescription();
        this.img_url = entity.getImgUrl();
        this.date = entity.getDate();
    }
    public ProductDTO(Product entity, Set<Category> categories){
        this(entity);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDTO> categories) {
        this.categories = categories;
    }
}
