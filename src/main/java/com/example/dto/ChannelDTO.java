package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelDTO extends BaseStringDTO{
    @NotBlank(message = "name is required")
    private String name;
    private String photoId;
    private String description;
    private String bannerId;
    private Integer profileId;
    private AttachDTO photo;
}
