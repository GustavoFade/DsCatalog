package com.example.dscatalog.services;

import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//Registra a classe como um componente que participa do sistema de injeção de dependencia
//no sprint
//pode colocar o @componente se for um componente generico(que não tenha um significado especifico)
//@repository se for um repository, vai da semantica
@Service
public class CategoryService {
    @Autowired // usado para injetar dependencia
    private CategoryRepository repository;

    public List<Category> findAll(){
        return repository.findAll();
    };
}
