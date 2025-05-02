package com.santeut.community.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @NotNull
    @Column(length = 15)
    private String userLoginId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String userPassword;

    @NotNull
    @Column(length = 15)
    private String userNickname;

    @NotNull
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime modifiedAt;

    @NotNull
    private Boolean isDeleted;

    @NotNull
    @Column(length = 9)
    private String userBirth;

    @NotNull
    private char userGender;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime deletedAt;

    @Column(columnDefinition = "TEXT")
    private String userProfile;

    @NotNull
    private int userAge;

    @NotNull
    private int userPoint;

    @NotNull
    private int userDistance;

    @NotNull
    private int userMoveTime;

    @NotNull
    private int userHikingCount;

    @NotNull
    private int userHikingMountain;

//    public static User signUp(SignUpRequestDto dto){
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.userNickname = dto.getUserNickname();
//        userEntity.userLoginId = dto.getUserLoginId();
//        userEntity.userPassword = dto.getUserPassword();
//        userEntity.userBirth = dto.getUserBirth();
//        userEntity.userProfile = null;
//        userEntity.userGender = dto.getUserGender();
//        userEntity.createdAt = LocalDateTime.now();
//        userEntity.modifiedAt = LocalDateTime.now();
//        userEntity.userPoint = 0;
//        userEntity.userDistance = 0;
//        userEntity.userMoveTime = 0;
//        userEntity.userHikingCount = 0;
//        userEntity.userHikingMountain = 0;
//        userEntity.isDeleted = false;
//
//        return userEntity;
//    }
}
