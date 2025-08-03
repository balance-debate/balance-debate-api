package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Comment
import java.time.LocalDateTime

data class ChildCommentGetResponse(
    val id: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(comments: List<Comment>): List<ChildCommentGetResponse> {
            return comments.map { from(it) }
        }

        private fun from(comment: Comment): ChildCommentGetResponse {
            return ChildCommentGetResponse(
                id = comment.id!!,
                content = comment.content,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
