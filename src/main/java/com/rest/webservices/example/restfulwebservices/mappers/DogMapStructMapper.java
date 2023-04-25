package com.rest.webservices.example.restfulwebservices.mappers;

import com.rest.webservices.example.restfulwebservices.model.DogDto;
import com.rest.webservices.example.restfulwebservices.model.DogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DogMapStructMapper {

    DogDto entityToDto(DogEntity entity);
    DogEntity dtoToEntity(DogDto dto);
}
