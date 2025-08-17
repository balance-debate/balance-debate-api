package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "comment_like",
    uniqueConstraints = [
        UniqueConstraint(
            columnNames = ["comment_id", "account_id"]
        )
    ]
)
class CommentLike(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    val comment: Comment,

    val accountId: Long,
) : BaseEntity() {

    protected constructor() : this(Comment(), 1L)
}
