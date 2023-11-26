package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Long id){
        super("Could not find task with " + id);
    }
}
