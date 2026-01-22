package com.archi.hexagonal.infrastructure.adapters.input.rest.exception;

public class UsernameNotFoundException extends RuntimeException{
    public UsernameNotFoundException(String msg){
        super(msg);
    }
}
