package com.example.dto;

import com.example.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCreateUpdateDTO {
    @NotBlank(message = "channel id is required")
    private String channelId;
    private NotificationType notificationType;
}
