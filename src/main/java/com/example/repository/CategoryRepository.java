package com.example.repository;

import com.example.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity,Integer> {
    Optional<CategoryEntity>findByNameAndVisibleTrue(String name);
    Optional<CategoryEntity>findByIdAndVisibleTrue(Integer id);
    List<CategoryEntity>findAllByVisibleTrue();
}
