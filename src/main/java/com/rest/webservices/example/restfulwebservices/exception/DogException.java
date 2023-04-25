package com.rest.webservices.example.restfulwebservices.exception;

import javax.xml.bind.ValidationException;

public class DogException extends ValidationException {

    public DogException(String message) {super(message);}
}
