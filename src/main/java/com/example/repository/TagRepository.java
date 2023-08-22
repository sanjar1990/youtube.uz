package com.example.repository;

import com.example.dto.TagDTO;
import com.example.entity.TagEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends CrudRepository<TagEntity,String > {
    Optional<TagEntity> findByIdAndVisibleTrue(String id);
    List<TagEntity> findAllByVisibleTrue();
    Optional<TagEntity>findByNameAndVisibleTrue(String name);
}
