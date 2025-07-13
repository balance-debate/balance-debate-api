package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
class Vote(
    @ManyToOne(fetch = FetchType.LAZY)
    val debate: Debate,

    val accountId: String,

    @Enumerated(EnumType.STRING)
    val target: VoteTarget,
) : BaseEntity() {

    protected constructor() : this(Debate(), "", VoteTarget.CHOICE_A)
}
