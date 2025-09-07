package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Comment
import com.balancedebate.api.web.dto.account.AccountResponse
import java.time.LocalDateTime

data class ChildCommentGetResponse(
    val id: Long,
    val content: String,
    val likeCount: Long,
    val liked: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val writer: AccountResponse,
) {
    companion object {
        fun from(comments: List<Comment>): List<ChildCommentGetResponse> {
            return comments.map { from(it) }
        }

        private fun from(comment: Comment): ChildCommentGetResponse {
            return ChildCommentGetResponse(
                id = comment.id!!,
                content = comment.content,
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
