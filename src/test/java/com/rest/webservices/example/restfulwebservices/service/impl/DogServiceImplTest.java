package com.rest.webservices.example.restfulwebservices.service.impl;

import com.rest.webservices.example.restfulwebservices.exception.DogException;
import com.rest.webservices.example.restfulwebservices.mappers.DogMapStructMapper;
import com.rest.webservices.example.restfulwebservices.model.DogDto;
import com.rest.webservices.example.restfulwebservices.model.DogEntity;
import com.rest.webservices.example.restfulwebservices.repository.DogRepository;
import com.rest.webservices.example.restfulwebservices.service.DogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class DogServiceImplTest {

    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = -1L;
    @InjectMocks
    private DogServiceImpl service;
    @Mock
    private DogRepository repository;

    @Mock
    private DogMapStructMapper mapper;

    private DogEntity dogEntity;

    private DogDto dogDto;

    private List<DogEntity> dogEntityList;

    private List<DogDto> dogDtoList;

    @BeforeEach
    public void setUp() {
        dogEntity = new DogEntity(
                VALID_ID,
                "sdfsf",
                "sfdfds",
                5
        );
        dogDto = new DogDto(
                VALID_ID,
                "sdfsf",
                "sfdfds",
                5
        );

        dogDtoList = new ArrayList<>(Arrays.asList(dogDto));
        dogEntityList = new ArrayList<>(Arrays.asList(dogEntity));

    }

    @Test
    public void saveDogTest() throws Exception {
        when(repository.save(dogEntity)).thenReturn(dogEntity);
        when(mapper.entityToDto(dogEntity)).thenReturn(dogDto);
        when(mapper.dtoToEntity(dogDto)).thenReturn(dogEntity);
        DogDto dogSaved = service.saveDog(dogDto);
        assertEquals(dogDto,dogSaved);
        verify(repository, times(1)).save(dogEntity);

    }
    @Test
    public void getAllDogsWhenHttpRequestSuccessful(){
        when(repository.findAll()).thenReturn(dogEntityList);
        when(mapper.entityToDto(dogEntity)).thenReturn(dogDto);

        List<DogDto> dogDtoList = service.findAll();

        assertEquals(1, dogDtoList.size());
        assertEquals(1, dogDtoList.get(0).getId());
        assertEquals("sdfsf", dogDtoList.get(0).getNickname());
    }

    @Test
    public void getAllWorkplacesWhenEmptyArrayList() {
        when(repository.findAll()).thenReturn(new ArrayList<>());

        List<DogDto> dogList = service.findAll();

        assertEquals(0, dogList.size());
        assertTrue(dogList.isEmpty());
    }

    @Test
    void testFindDogByValidId() throws Exception {
        when(repository.findById(VALID_ID)).thenReturn(Optional.of(dogEntity));
        when(mapper.entityToDto(dogEntity)).thenReturn(dogDto);
        Optional<DogDto> returnedDog = service.findDogById(dogDto.getId());
        assertEquals(dogDto.getId(), returnedDog.get().getId());
        assertEquals(dogDto.getNickname(), returnedDog.get().getNickname());
        assertEquals(dogDto.getBreed(), returnedDog.get().getBreed());
        assertEquals(dogDto.getAge(), returnedDog.get().getAge());
        verify(repository, times(1)).findById(VALID_ID);
    }
    @Test
    void testFindDogByInvalidId() throws Exception {
        when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
        Assertions.assertFalse(service.findDogById(INVALID_ID).isPresent());
        verify(repository, times(1)).findById(INVALID_ID);
    }


    @Test
    public void testDeleteDogByInvalidId3() throws DogException {
        // given
        Long invalidId = INVALID_ID;
        given(repository.findById(invalidId)).willReturn(Optional.empty());

        // when, then
        assertThrows(DogException.class, () -> service.deleteDogById(invalidId), "Dog not found with id: " + invalidId);
    }

}
