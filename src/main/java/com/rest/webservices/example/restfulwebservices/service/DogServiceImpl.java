package com.rest.webservices.example.restfulwebservices.service;

import com.rest.webservices.example.restfulwebservices.mappers.DogMapStructMapper;
import com.rest.webservices.example.restfulwebservices.model.DogDto;
import com.rest.webservices.example.restfulwebservices.model.DogEntity;
import com.rest.webservices.example.restfulwebservices.exception.DogException;

import com.rest.webservices.example.restfulwebservices.repository.DogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    @Autowired
    DogRepository repository;
    @Autowired
    DogMapStructMapper mapper;

    @Override
    public Optional<DogDto> findDogById(Long id) {
        Optional<DogEntity> dog = repository.findById(id);
        return dog.map(mapper::entityToDto);
    }

    @Override
    public DogDto saveDog(DogDto dogDto) throws Exception {
        if (hasAnyMatch(dogDto)) {
            log.error("Dog conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        DogEntity dogEntity= repository.save(mapper.dtoToEntity(dogDto));
        log.info("New dog saved: {}", dogEntity);
        return mapper.entityToDto(dogEntity);
    }

    public boolean hasAnyMatch(DogDto dogDto){
        return repository.findAll().stream()
                .anyMatch( e -> e.getNickname().equals(dogDto.getNickname())
                && e.getAge() == dogDto.getAge()
                && e.getBreed().equals(dogDto.getBreed())
                );
    }

    @Override
    public List<DogDto> findAll() {
        List<DogEntity> dogEntities = repository.findAll();
        return dogEntities.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteDogById(Long id) throws DogException {
        Optional<DogEntity> dogEntity = repository.findById(id);
        if (!dogEntity.isPresent()) {
            throw new DogException("Dog not found with id: " + id);
        }
        repository.deleteById(id);
    }

}
