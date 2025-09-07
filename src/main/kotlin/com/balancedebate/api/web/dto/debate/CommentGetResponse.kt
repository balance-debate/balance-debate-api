package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Comment
import com.balancedebate.api.web.dto.account.AccountResponse
import java.time.LocalDateTime

data class CommentGetResponse(
    val id: Long,
    val content: String,
    val childComments: List<ChildCommentGetResponse> = emptyList(),
    val likeCount: Long,
    val liked: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: AccountResponse,
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
                liked = comment.liked,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                writer = AccountResponse(
                    nickname = comment.nickname,
                    profileEmoji = comment.profileEmoji,
                ),
            )
        }
    }
}
