package com.balancedebate.api.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    @Column
    var updatedAt: LocalDateTime? = null,
)
