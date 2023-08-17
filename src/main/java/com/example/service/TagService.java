package com.example.service;
import com.example.dto.ApiResponseDTO;
import com.example.dto.TagDTO;
import com.example.entity.TagEntity;
import com.example.exception.ItemNotFoundException;
import com.example.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;
    //       1. Create Tag
    public ApiResponseDTO create(TagDTO dto) {
        TagEntity entity=new TagEntity();
        entity.setName(dto.getName());
        tagRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return new ApiResponseDTO(false,dto);
    }
    //      2. Update Tag (ADMIN)
    public ApiResponseDTO update(String name, String id) {
        TagEntity entity=get(id);
        entity.setName(name);
        tagRepository.save(entity);
        return new ApiResponseDTO(false,toDto(entity));
    }

    //      3. Delete Tag (ADMIN
    public ApiResponseDTO delete(String id) {
        TagEntity entity=get(id);
        entity.setVisible(false);
        tagRepository.save(entity);
        return new ApiResponseDTO(false,"deleted");
    }
    //      4. Tag List
    public List<TagDTO> getAll() {
        List<TagEntity> entityList=tagRepository.findAllByVisibleTrue();
        return entityList.stream().map(s->toDto(s)).toList();
    }
    private TagDTO toDto(TagEntity entity){
        TagDTO dto=new TagDTO();
        dto.setName(entity.getName());
        dto.setId(entity.getId());
        return dto;
    }
    private TagEntity get(String id){
        return tagRepository.findByIdAndVisibleTrue(id)
                .orElseThrow(()->new ItemNotFoundException("Category not found"));
    }

}
