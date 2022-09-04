package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);

        Page<CategoryDTO> listDto = list.map( x -> new CategoryDTO(x));

//        list.forEach(category -> listDto.add(new CategoryDTO(category)));
        return listDto;
    };

    @Transactional(readOnly = true)
    public CategoryDTO findCategoryById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(()-> new ResourceNotFoundException("Category not found !"));
        return new CategoryDTO(entity);
    }
    @Transactional
    public CategoryDTO insertCategory(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO updateCategory(long id, CategoryDTO dto) {
        try {
            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Category not found, update failed ! id: " + id);
        }

    }

    public void delete(long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Category not found, delete failed ! id: " + id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation !");
        }
    }
}
