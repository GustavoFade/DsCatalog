package com.example.dscatalog.resources;

import com.example.dscatalog.dto.UserDTO;
import com.example.dscatalog.dto.UserInsertDTO;
import com.example.dscatalog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/users")
public class UserResources {
    //resource implementa o controlador rest - recurso

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "id") String orderBy
    ){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        Page<UserDTO> list = service.findAllPaged(pageRequest);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable long id){
        UserDTO categoryDto = service.findUserById(id);
        return ResponseEntity.ok().body(categoryDto);
    }
    @PostMapping
    public ResponseEntity<UserDTO> insertUser(@Valid @RequestBody UserInsertDTO dto){
        UserDTO newDto = service.insertUser(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable long id, @Valid @RequestBody UserDTO dto){
        dto = service.updateUser(id, dto);
        return ResponseEntity.ok().body(dto);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
