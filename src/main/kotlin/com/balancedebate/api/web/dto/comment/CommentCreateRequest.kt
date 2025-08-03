package com.balancedebate.api.web.dto.comment

data class CommentCreateRequest (
    val parentCommentId: Long?,
    val content: String,
)