package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Comment
import java.time.LocalDateTime

data class CommentGetResponse(
    val id: Long,
    val content: String,
    val childComments: List<ChildCommentGetResponse> = emptyList(),
    val likeCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(comments: List<Comment>): List<CommentGetResponse> {
            return comments.map { from(it) }
        }

        private fun from(comment: Comment): CommentGetResponse {
            return CommentGetResponse(
                id = comment.id!!,
                content = comment.content,
                childComments = ChildCommentGetResponse.from(comment.childComments),
                likeCount = comment.likeCount,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
