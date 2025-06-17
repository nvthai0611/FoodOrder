package com.foodorder.backend.example;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "examples")
public class ExampleEntity {
    @Id
    private String id;

    private String name;
}
