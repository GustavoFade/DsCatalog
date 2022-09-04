package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.ProductRepository;
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
import java.util.Optional;

//Registra a classe como um componente que participa do sistema de injeção de dependencia
//no sprint
//pode colocar o @componente se for um componente generico(que não tenha um significado especifico)
//@repository se for um repository, vai da semantica
@Service
public class ProductService {
    @Autowired // usado para injetar dependencia
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //se nesse faz varias operações e todas essas estão envolvidas com o banco e quero ter a integridade da transação, coloca a @transactional
    @Transactional(readOnly = true)//quando é apenas leitura usa isso para ser mais rapido, não trava o bd
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = productRepository.findAll(pageRequest);

        Page<ProductDTO> listDto = list.map( x -> new ProductDTO(x, x.getCategories()));

//        list.forEach(category -> listDto.add(new ProductDTO(category)));
        return listDto;
    };

    @Transactional(readOnly = true)
    public ProductDTO findProductById(Long id){
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Product not found !"));
        return new ProductDTO(entity, entity.getCategories());
    }
    @Transactional
    public ProductDTO insertProduct(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto,entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO updateProduct(long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getOne(id);
            copyDtoToEntity(dto,entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity,entity.getCategories());
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Product not found, update failed ! id: " + id);
        }

    }

    public void delete(long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Product not found, delete failed ! id: " + id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation !");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImg_url());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO categoryDTO: dto.getCategories()) {
            Category category = categoryRepository.getOne(categoryDTO.getId());
            entity.getCategories().add(category);
        }
    }
}
