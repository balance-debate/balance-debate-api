package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
class Vote(
    @ManyToOne(fetch = FetchType.LAZY)
    val debate: Debate,

    val accountId: String,
) : BaseEntity() {

    protected constructor() : this(Debate(), "")
}
