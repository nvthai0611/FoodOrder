package com.foodorder.backend.example;

import com.foodorder.backend.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends BaseRepository<ExampleEntity, Long> {
}
