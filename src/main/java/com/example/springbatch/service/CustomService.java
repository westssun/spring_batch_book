package com.example.springbatch.service;

import org.springframework.stereotype.Service;

@Service
public class CustomService {
    public void serviceMethod(String message) {
        System.out.println("service method was called : " + message);
    }
}
