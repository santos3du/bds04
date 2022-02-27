package br.com.eduardo.bds04.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.eduardo.bds04.dto.CityDTO;
import br.com.eduardo.bds04.entities.City;
import br.com.eduardo.bds04.repositories.CityRepository;
import br.com.eduardo.bds04.services.exceptions.DatabaseException;
import br.com.eduardo.bds04.services.exceptions.ResourceNotFoundException;


@Service
public class CityService {

	@Autowired
	private CityRepository repository;
	
	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		List<City> list = repository.findAll(Sort.by("name"));
		return list.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public CityDTO insert( CityDTO dto) {
		City entity = new City();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CityDTO(entity);
	}
	
	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = new City();
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CityDTO(entity);
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
	public CityDTO findById(Long id) {
		Optional<City> optional = repository.findById(id);
		City city = optional.orElseThrow(() -> new ResourceNotFoundException("Id not found. "));
		return new CityDTO(city);
	}

}
