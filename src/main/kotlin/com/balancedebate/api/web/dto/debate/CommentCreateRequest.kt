package com.balancedebate.api.web.dto.debate

data class CommentCreateRequest (
    val parentCommentId: Long?,
    val content: String,
)