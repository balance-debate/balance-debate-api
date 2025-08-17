package com.balancedebate.api.web.controller

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.service.CommentLikeService
import com.balancedebate.api.web.config.LoginAccount
import com.balancedebate.api.web.dto.ApiResponse
import com.balancedebate.api.web.dto.debate.*
import org.springframework.web.bind.annotation.*

@RestController
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {

    @PostMapping("/comments/{commentId}/likes")
    fun createCommentLike(@PathVariable commentId: Long, @LoginAccount account: Account): ApiResponse<Unit> {
        commentLikeService.createCommentLike(commentId, account)
        return ApiResponse.success()
    }

    @DeleteMapping("/comments/{commentId}/likes")
    fun deleteCommentLike(@PathVariable commentId: Long, @LoginAccount account: Account): ApiResponse<Unit> {
        commentLikeService.deleteCommentLike(commentId, account)
        return ApiResponse.success()
    }
}
