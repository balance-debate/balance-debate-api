package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CommentMetaRepository : JpaRepository<CommentMeta, Long> {

    fun findByComment(comment: Comment): Optional<CommentMeta>
}
