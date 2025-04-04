package com.christian.userservice.user_service.exceptions.custom;

public class UsernameNotFoundException extends RuntimeException{

    public UsernameNotFoundException(String message){
        super(message);
    }

}
