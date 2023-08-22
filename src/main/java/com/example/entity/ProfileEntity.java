package com.example.entity;

import com.example.enums.Language;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseIntEntity {
    @Column
    private String name;
    @Column
    private String surname;
    @Column(name = "email",unique = true)
    private String email;
    @Column
    private String password;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id", updatable = false, insertable = false)
    private AttachEntity attach;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProfileStatus status;
    @Column(name = "prt_id")
    private Integer prtId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prt_id",updatable = false,insertable = false)
    private ProfileEntity profile;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
}
