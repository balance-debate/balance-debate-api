package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import com.balancedebate.api.domain.account.Account
import jakarta.persistence.*

@Entity
class Debate(
    @Column(nullable = false)
    val topic: String,

    @Column(nullable = false, name = "choice_a")
    val choiceA: String,

    @Column(nullable = false, name = "choice_b")
    val choiceB: String,

    @Column(nullable = false, name = "thumbnail_url")
    val thumbnailUrl: String,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "debate", cascade = [CascadeType.PERSIST])
    val votes: MutableSet<Vote> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "debate", cascade = [CascadeType.PERSIST])
    val comments: MutableSet<Comment> = mutableSetOf(),
) : BaseEntity() {

    fun add(vote: Vote) {
        this.votes.add(vote)
    }

    fun addComment(accountId: Long, content: String) {
        val comment = Comment(this, accountId, content)
        this.comments.add(comment)
    }

    constructor() : this("", "", "", "")
}
