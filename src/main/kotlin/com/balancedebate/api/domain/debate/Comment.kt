package com.balancedebate.api.domain.debate

import com.balancedebate.api.domain.BaseEntity
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import jakarta.persistence.*
import kotlin.jvm.Transient

@Entity
class Comment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debate_id", nullable = false)
    val debate: Debate,

    val accountId: Long?,

    @Column(nullable = false)
    val content: String,

    @Column
    val parentCommentId: Long? = null,

) : BaseEntity() {

    @Transient
    lateinit var childComments: List<Comment>

    @Transient
    var likeCount: Long = 0

    companion object {
        private const val MAX_CONTENT_LENGTH = 500
    }

    init {
        if (content.isBlank()) {
            throw ApiException(errorReason = ErrorReason.BAD_REQUEST, message = "Comment content must not be blank.")
        }

        if (content.length > MAX_CONTENT_LENGTH) {
            throw ApiException(errorReason = ErrorReason.BAD_REQUEST, message = "Comment content must not exceed $MAX_CONTENT_LENGTH characters.")
        }
    }

    constructor() : this(Debate(), 1L, "댓글")
}
