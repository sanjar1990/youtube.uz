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
@Table(name = "tag")
public class TagEntity extends BaseStringEntity {
    @Column(name = "name", unique = true)
    private String name;
}
