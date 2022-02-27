package br.com.eduardo.bds04.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.eduardo.bds04.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String name);

}
