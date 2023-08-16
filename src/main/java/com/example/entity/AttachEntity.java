package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attach")
public class AttachEntity  {
    @Id
    private String id;
    @Column(name = "origin_name")
    private String originName;
    @Column
    private Long size;
    @Column
    private String extension;
    @Column
    private String path;
    @Column
    private Long duration;
    @Column
    private Boolean visible=true;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

}
