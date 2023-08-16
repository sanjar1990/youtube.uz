package com.example.entity;

import com.example.dto.BaseStringDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BaseStringEntity {
    @Column(name = "to_email")
    private String toEmail;
    @Column(name = "title",columnDefinition = "TEXT")
    private String title;
    @Column(name = "message",columnDefinition = "TEXT")
    private String message;
    }
