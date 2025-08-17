package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {

    fun existsByCommentAndAccountId(comment: Comment, id: Long): Boolean
}
