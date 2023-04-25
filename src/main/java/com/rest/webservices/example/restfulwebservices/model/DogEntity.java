package com.rest.webservices.example.restfulwebservices.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dog")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description = "Details about dog")
public class DogEntity {

    @Id
    @GeneratedValue
    @Column(name="dog_id")
    @ApiModelProperty(notes ="Unique id of a dog")
    private Long id;

    @Column(name="dog_nickname")
    @ApiModelProperty(notes = "Nickname of a dog")
    private String nickname;

    @Column(name="dog_breed")
    @ApiModelProperty(notes = "Breed of a dog")
    private String breed;

    @Column(name="dog_age")
    @ApiModelProperty(notes = "Age of a dog")
    private int age;


}
