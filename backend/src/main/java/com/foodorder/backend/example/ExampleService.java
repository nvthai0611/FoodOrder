package com.foodorder.backend.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    @Autowired
    private ExampleRepository repository;

}
