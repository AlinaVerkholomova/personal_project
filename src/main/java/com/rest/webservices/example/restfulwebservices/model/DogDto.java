package com.rest.webservices.example.restfulwebservices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@Validated
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model of dog data")
public class DogDto {

    @ApiModelProperty(notes = "Unique id of a dog")
    private Long id;

    @Size(min=2, message="Nickname should have at least 2 characters")
    @NotBlank
    @ApiModelProperty(notes = "Nickname of a dog")
    private String nickname;

    @NotBlank
    @ApiModelProperty(notes = "Breed of a dog")
    private String breed;

    @NotNull
    @ApiModelProperty(notes = "Age of a dog")
    private int age;

}
