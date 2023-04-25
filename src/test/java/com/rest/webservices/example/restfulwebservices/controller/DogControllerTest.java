package com.rest.webservices.example.restfulwebservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.webservices.example.restfulwebservices.controllers.DogController;
import com.rest.webservices.example.restfulwebservices.model.DogDto;
import com.rest.webservices.example.restfulwebservices.service.DogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyLong;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogController.class)
public class DogControllerTest {

    public static String URL = "/api/v1/dogs";
    private static final Long VALID_ID = 1L;

    @MockBean
    private DogService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private DogDto dogDto;

    private List<DogDto> dogDtoList;

    private Optional<DogDto> dogDtoOptional;

    @BeforeEach
    public void setUp(){
        dogDto = new DogDto();
        dogDto.setId(VALID_ID);
        dogDto.setNickname("Archie");
        dogDto.setBreed("none");
        dogDto.setAge(2);

        dogDtoList = new ArrayList<>(Arrays.asList(dogDto));
        dogDtoOptional = Optional.of(dogDto);
    }

    @Test
    public void getAllDogsHttpRequest() throws Exception {

        when(service.findAll()).thenReturn(dogDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dogDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(VALID_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nickname").value("Archie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].breed").value("none"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(2))
                .andReturn();
        verify(service, times(1)).findAll();
    }

    @Test
    public void getAllDogsHttpEmptyResponse() throws Exception {
        List<DogDto> dogDto = new ArrayList<>();
        when(service.findAll()).thenReturn(dogDto);

        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void saveDogHttpRequest() throws Exception {
        when(service.saveDog(dogDto)).thenReturn(dogDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dogDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(VALID_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Archie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("none"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(2))
                .andReturn();

        verify(service, times(1)).saveDog(dogDto);
    }

    @Test
    public void saveDogsHttpRequestEmpty() throws Exception {
        dogDto = new DogDto();

        when(service.saveDog(dogDto)).thenReturn(dogDto);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testFindDogByIdValid() throws Exception {
        when(service.findDogById(VALID_ID)).thenReturn(dogDtoOptional);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(VALID_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("Archie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.breed").value("none"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(2))
                .andExpect(status().isOk());

        verify(service, times(1)).findDogById(VALID_ID);
    }

    @Test
    void testFindDogByIdInvalid() throws Exception {
        dogDtoOptional.get().setId(null);

        when(service.findDogById(null)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + null)
                        .content(asJsonString(dogDtoOptional))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).findDogById(null);
    }

    @Test
    void testDeleteDogValidId() throws Exception {
        when(service.findDogById(anyLong())).thenReturn(dogDtoOptional);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1").content(asJsonString(dogDtoOptional))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(service, times(1)).deleteDogById(anyLong());

    }

    @Test
    void testDeleteDogInvalidId() throws Exception {
        dogDtoOptional.get().setId(null);

        when(service.findDogById(null)).thenReturn(dogDtoOptional);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + null)
                        .content(asJsonString(dogDtoOptional))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, times(0)).deleteDogById(null);
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);


        }
    }

}
