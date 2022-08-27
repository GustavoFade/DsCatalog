package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Registra a classe como um componente que participa do sistema de injeção de dependencia
//no sprint
//pode colocar o @componente se for um componente generico(que não tenha um significado especifico)
//@repository se for um repository, vai da semantica
@Service
public class CategoryService {
    @Autowired // usado para injetar dependencia
    private CategoryRepository repository;

    //se nesse faz varias operações e todas essas estão envolvidas com o banco e quero ter a integridade da transação, coloca a @transactional
    @Transactional(readOnly = true)//quando é apenas leitura usa isso para ser mais rapido, não trava o bd
    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();

        List<CategoryDTO> listDto = list.stream().map( x -> new CategoryDTO(x)).collect(Collectors.toList());

//        list.forEach(category -> listDto.add(new CategoryDTO(category)));
        return listDto;
    };
}
