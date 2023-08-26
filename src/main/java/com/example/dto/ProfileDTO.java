package com.example.dto;
import com.example.enums.ProfileRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
 private Integer id;
   @NotNull(message = "name is required")
   @Size(min = 3, message = "name character should be more then 3")
    private String name;
    @NotNull(message = "surname is required")
    @Size(min = 3, message = "surname character should be more then 3")
    private String surname;
    @Email(message = "enter valid email")
    private String email;
    @NotNull(message = "password is required")
    @Size(min = 4, message = "password character should be more then 4")
    private String password;
    private ProfileRole role;
    private String attachId;
    private String attachUrl;
    private String jwt;
    private AttachDTO attach;

}
