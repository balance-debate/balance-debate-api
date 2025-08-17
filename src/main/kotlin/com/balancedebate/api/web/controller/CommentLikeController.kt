package com.balancedebate.api.web.controller

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.service.CommentLikeService
import com.balancedebate.api.web.config.LoginAccount
import com.balancedebate.api.web.dto.ApiResponse
import com.balancedebate.api.web.dto.debate.*
import org.springframework.web.bind.annotation.*

/**
 * 1. 댓글 좋아요 API
 * 2. 댓글 좋아요 취소 API
 * 3. 댓글 응답 목록에 좋아요 개수 추가(commentMeta 테이블 생성)
 * 4. 댓글 응답 목록에 내가 좋아요 했는지 여부 추가
 */
@RestController
class CommentLikeController(
    private val commentLikeService: CommentLikeService
) {

    @PostMapping("/comments/{commentId}/likes")
    fun likeComment(@PathVariable commentId: Long, @LoginAccount account: Account): ApiResponse<Unit> {
        commentLikeService.likeComment(commentId, account)
        return ApiResponse.success()
    }
}
