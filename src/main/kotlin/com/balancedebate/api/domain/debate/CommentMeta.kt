package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*

@Entity
class CommentMeta(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    val comment: Comment,

    var likeCount: Long = 0L,
) : BaseEntity() {

    protected constructor() : this(Comment())
}
