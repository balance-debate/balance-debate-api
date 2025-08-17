package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {

    fun existsByCommentAndAccountId(comment: Comment, accountId: Long): Boolean

    fun findByCommentAndAccountId(comment: Comment, accountId: Long): Optional<CommentLike>
}
