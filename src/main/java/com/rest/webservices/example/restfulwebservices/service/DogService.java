package com.rest.webservices.example.restfulwebservices.service;

import com.rest.webservices.example.restfulwebservices.exception.DogException;
import com.rest.webservices.example.restfulwebservices.model.DogDto;


import java.util.List;
import java.util.Optional;

public interface DogService {

    Optional<DogDto> findDogById(Long id);

    public DogDto saveDog(DogDto dogDto) throws Exception;


    public List<DogDto> findAll();

    void deleteDogById(Long id) throws DogException;
}
