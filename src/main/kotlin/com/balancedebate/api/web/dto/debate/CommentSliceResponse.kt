package com.balancedebate.api.web.dto.debate

data class CommentSliceResponse(
    val hasNext: Boolean,
    val comments: List<CommentGetResponse>,
)