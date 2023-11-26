package com.example.demo.exception;

public class MessageNotReadableException extends RuntimeException{
    public  MessageNotReadableException(){
        super("Request is malformed");
    }
}