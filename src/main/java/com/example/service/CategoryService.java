package com.example.service;

import com.example.dto.ApiResponseDTO;
import com.example.dto.CategoryDTO;
import com.example.entity.CategoryEntity;
import com.example.exception.ItemExistsException;
import com.example.exception.ItemNotFoundException;
import com.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    //      1. Create Category (ADMIN)
    public ApiResponseDTO create(CategoryDTO dto) {
       nameExists(dto.getName());
        CategoryEntity entity=new CategoryEntity();
        entity.setName(dto.getName());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return new ApiResponseDTO(false,dto);
    }
    //    2. Update Category (ADMIN)
    public ApiResponseDTO update(String name, Integer id) {
        nameExists(name);
        CategoryEntity entity=get(id);
       entity.setName(name);
       categoryRepository.save(entity);
       return new ApiResponseDTO(false,toDto(entity));
    }

    //    3. Delete Category (ADMIN)
    public ApiResponseDTO delete(Integer id) {
        CategoryEntity entity=get(id);
        entity.setVisible(false);
        categoryRepository.save(entity);
        return new ApiResponseDTO(false,"deleted");
    }
    //    4. Category List
    public List<CategoryDTO> getAll() {
        List<CategoryEntity> entityList=categoryRepository.findAllByVisibleTrue();
        return entityList.stream().map(this::toDto).toList();
    }
    private CategoryDTO toDto(CategoryEntity entity){
        CategoryDTO dto=new CategoryDTO();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        return dto;
    }
    private CategoryEntity get(Integer id){
        return categoryRepository.findByIdAndVisibleTrue(id).orElseThrow(()->new ItemNotFoundException("Category not found"));
    }
    private void nameExists(String name){
        Optional<CategoryEntity>optional=categoryRepository.findByNameAndVisibleTrue(name);
        if(optional.isPresent())throw new ItemExistsException("Category name already exists");
    }




}
