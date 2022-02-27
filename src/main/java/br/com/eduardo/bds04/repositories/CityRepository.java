package br.com.eduardo.bds04.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.eduardo.bds04.entities.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long>{

}
