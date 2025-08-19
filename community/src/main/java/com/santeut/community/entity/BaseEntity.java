package com.santeut.community.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass   //JPA Entity 클래스들이 해당 추상 클래스를 상속할 경우 createDate, modifiedDate를 컬럼으로 인식
@EntityListeners(AuditingEntityListener.class)  //해당 클래스에 Auditing 기능을 포함
public abstract class BaseEntity {

    @CreationTimestamp
    @NotNull
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @NotNull
    @Column(name="is_deleted", columnDefinition = "TINYINT DEFAULT 0")
    private boolean isDeleted;

    /**
     * 비즈니스 로직
     **/
    //소프트딜리트(삭제)용
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
        if (isDeleted) {
            deletedAt = LocalDateTime.now();
        } else {
            deletedAt = null;
        }
    }

}
