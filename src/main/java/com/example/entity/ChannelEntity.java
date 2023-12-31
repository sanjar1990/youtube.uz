package com.example.entity;
import com.example.enums.ActiveBlockStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class ChannelEntity extends BaseStringEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "photo_id")
    private String photoId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id",insertable = false,updatable = false)
    private AttachEntity photo;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column
    private ActiveBlockStatus status;
    @Column(name = "banner_id")
    private String bannerId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id",insertable = false,updatable = false)
    private AttachEntity banner;
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false,updatable = false)
    private ProfileEntity profile;
}
