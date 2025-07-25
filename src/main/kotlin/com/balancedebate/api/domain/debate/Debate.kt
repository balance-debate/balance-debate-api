package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
class Debate(
    @Column(nullable = false)
    val topic: String,

    @Column(nullable = false, name = "choice_a")
    val choiceA: String,

    @Column(nullable = false, name = "choice_b")
    val choiceB: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "debate", cascade = [CascadeType.PERSIST])
    val votes: MutableSet<Vote> = mutableSetOf()
) : BaseEntity() {

    fun add(vote: Vote) {
        this.votes.add(vote)
    }

    constructor() : this("", "", "")
}
