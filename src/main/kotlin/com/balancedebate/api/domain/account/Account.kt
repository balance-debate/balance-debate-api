package com.balancedebate.api.domain.account

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "account",
    uniqueConstraints = [UniqueConstraint(columnNames = ["nickname"])]
)
 class Account(
    @Column(nullable = false, unique = true)
    val nickname: String,

    @Column(nullable = false)
    val password: String,
) : BaseEntity()
