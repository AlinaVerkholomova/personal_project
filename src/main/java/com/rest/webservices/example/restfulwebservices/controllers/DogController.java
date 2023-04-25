package com.rest.webservices.example.restfulwebservices.controllers;

import com.rest.webservices.example.restfulwebservices.model.DogDto;
import com.rest.webservices.example.restfulwebservices.service.DogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.rest.webservices.example.restfulwebservices.swagger.SwaggerTagStore.DOG_CONTROLLER_TAG_NAME;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/dogs")
@RequiredArgsConstructor
@Slf4j
@Api(tags = DOG_CONTROLLER_TAG_NAME)
public class DogController {

    private final DogService service;
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<DogDto> findDogById(@ApiParam(value = "id of the dog", required = true)
                                              @NonNull @PathVariable Long id) {
        log.info("Find Dog by passing ID, where ID is :{} ", id);
        Optional<DogDto> dogDto = (service.findDogById(id));
        if(!(dogDto.isPresent())) {
            log.warn("Dog with id {} not found", id);
        } else {
            log.debug("Dog with id {} found", id);
        }
        return dogDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the dog by id",
            notes = "Deletes the dog if provided id exists",
            response = DogDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The dog is successfully deleted"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDogById(@ApiParam(value = "The id of the dog", required = true)
                                                 @NonNull @PathVariable Long id) throws Exception {
        log.info("Delete Dog by passing ID, where ID is:{}", id);
        Optional<DogDto> dogDto = (service.findDogById(id));
        if(!(dogDto.isPresent())){
            log.warn("Dog with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        service.deleteDogById(id);
        log.debug("Dog with id {} is deleted: {}", id, dogDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ApiOperation(value = "Finds all dogs",
            notes = "Returns the entire list of dogs",
            response = DogDto.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = DogDto.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public List<DogDto> findAllDogs(){
        return service.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Server cannot find the requested resource"),
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ApiOperation(value = "Add dog",
            notes = "If provided valid dog, saves it",
            response = DogDto.class)
    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DogDto> addDog(@RequestBody @Valid DogDto dogDto, BindingResult bindingResult) throws Exception {

        if(bindingResult.hasErrors()){
            log.error("Dog is not added: error: {}", bindingResult);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DogDto dog = service.saveDog(dogDto);
        log.debug("Dog is saved: {}", dog);
        return new ResponseEntity<>(dogDto, HttpStatus.CREATED);
    }

}

