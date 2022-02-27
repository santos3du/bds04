package br.com.eduardo.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.eduardo.bds04.dto.RoleDTO;
import br.com.eduardo.bds04.dto.UserDTO;
import br.com.eduardo.bds04.dto.UserUpdateDTO;
import br.com.eduardo.bds04.entities.Role;
import br.com.eduardo.bds04.entities.User;
import br.com.eduardo.bds04.repositories.RoleRepository;
import br.com.eduardo.bds04.repositories.UserRepository;
import br.com.eduardo.bds04.services.exceptions.DatabaseException;
import br.com.eduardo.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class UserService implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional
	public UserDTO insert( UserDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new UserDTO(entity);
	}
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
		
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new UserDTO(entity);
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found. " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
			
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found. " + id);
		}catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violarion");
		}
		
	}
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> optional = repository.findById(id);
		User Role = optional.orElseThrow(() -> new ResourceNotFoundException("Id not found. "));
		return new UserDTO(Role);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		logger.info(user.getEmail());
		if(user == null){
            logger.error("User not found: "+ username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("User get log in: " + user.getEmail());
        return  user;
	}

}
