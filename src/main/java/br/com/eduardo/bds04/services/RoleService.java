package br.com.eduardo.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.eduardo.bds04.dto.RoleDTO;
import br.com.eduardo.bds04.entities.Role;
import br.com.eduardo.bds04.repositories.RoleRepository;
import br.com.eduardo.bds04.services.exceptions.DatabaseException;
import br.com.eduardo.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class RoleService {

	@Autowired
	private RoleRepository repository;
	
	@Transactional(readOnly = true)
	public Page<RoleDTO> findAll(Pageable pageable) {
		Page<Role> list = repository.findAll(pageable);
		return list.map(x -> new RoleDTO(x));
	}
	
	@Transactional
	public RoleDTO insert( RoleDTO dto) {
		Role entity = new Role();
		entity.setAuthority(dto.getAuthority());;
		entity = repository.save(entity);
		return new RoleDTO(entity);
	}
	
	@Transactional
	public RoleDTO update(Long id, RoleDTO dto) {
		try {
			Role entity = new Role();
			entity.setAuthority(dto.getAuthority());;
			entity = repository.save(entity);
			return new RoleDTO(entity);
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
	public RoleDTO findById(Long id) {
		Optional<Role> optional = repository.findById(id);
		Role Role = optional.orElseThrow(() -> new ResourceNotFoundException("Id not found. "));
		return new RoleDTO(Role);
	}

}
