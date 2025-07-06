package com.balancedebate.api.domain.account

import com.balancedebate.api.domain.BaseEntity
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import jakarta.persistence.*
import java.io.Serializable

@Entity
class Debate(
    @Column(nullable = false)
    val topic: String,

    @Column(nullable = false, name = "choice_a")
    val choiceA: String,

    @Column(nullable = false, name = "choice_b")
    val choiceB: String,
) : BaseEntity() {

    protected constructor() : this("", "", "")
}
