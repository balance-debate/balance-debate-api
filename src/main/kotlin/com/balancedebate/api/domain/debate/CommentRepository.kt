package com.balancedebate.api.domain.debate

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {

    fun findByDebate(debate: Debate, pageable: Pageable): Slice<Comment>

    fun findByParentCommentIdIn(parentCommentIds: List<Long>): List<Comment>
}
