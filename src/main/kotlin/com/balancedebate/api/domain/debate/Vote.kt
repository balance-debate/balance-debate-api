package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
class Vote(
    @ManyToOne(fetch = FetchType.LAZY)
    val debate: Debate,

    val uuid: String?,

    val accountId: Long?,

    @Enumerated(EnumType.STRING)
    val target: VoteTarget,
) : BaseEntity() {

    protected constructor() : this(Debate(), null, null, VoteTarget.CHOICE_A)
}
