package com.rest.webservices.example.restfulwebservices.repository;

import com.rest.webservices.example.restfulwebservices.model.DogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<DogEntity, Long> {
}
