package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO extends BaseStringDTO {
    @NotNull(message = "name is required")
    private String name;

    public TagDTO(String tagName, String tagId){
        this.setName(tagName);
        this.setId(tagId);
    }

}
