package com.example.dscatalog.services;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.dto.RoleDTO;
import com.example.dscatalog.dto.UserDTO;
import com.example.dscatalog.dto.UserInsertDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Role;
import com.example.dscatalog.entities.User;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.RoleRepository;
import com.example.dscatalog.repositories.UserRepository;
import com.example.dscatalog.services.exceptions.DataBaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(PageRequest pageRequest){
        Page<User> list = userRepository.findAll(pageRequest);

        Page<UserDTO> listDto = list.map( x -> new UserDTO(x));
//        Page<UserDTO> listDto = list.map(UserDTO::new);

//        list.forEach(category -> listDto.add(new UserDTO(category)));
        return listDto;
    };

    @Transactional(readOnly = true)
    public UserDTO findUserById(Long id){
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(()-> new ResourceNotFoundException("User not found !"));
        return new UserDTO(entity);
    }
    @Transactional
    public UserDTO insertUser(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto,entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO updateUser(long id, UserDTO dto) {
        try {
            User entity = userRepository.getOne(id);
            copyDtoToEntity(dto,entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("User not found, update failed ! id: " + id);
        }

    }

    public void delete(long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("User not found, delete failed ! id: " + id);
        } catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation !");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());

        entity.getRoles().clear();
        for (RoleDTO roleDTO: dto.getRoles()) {
            Role role = roleRepository.getOne(roleDTO.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null){
            throw new UsernameNotFoundException("Email not found");
        }
        return user;
    }
}
