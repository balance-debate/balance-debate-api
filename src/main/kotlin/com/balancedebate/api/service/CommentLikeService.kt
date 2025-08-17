package com.balancedebate.api.service

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.domain.account.AccountRepository
import com.balancedebate.api.domain.debate.*
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CommentLikeService(
    private val commentLikeRepository: CommentLikeRepository,
    private val commentRepository: CommentRepository,
    private val commentMetaRepository: CommentMetaRepository,
    private val accountRepository: AccountRepository,
) {

    fun likeComment(commentId: Long, account: Account) {
        val comment = commentRepository.findById(commentId).orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Comment with id $commentId not found", "NOT_FOUND_COMMENT") }

        val loginUser = accountRepository.findByNickname(account.nickname)
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Account with nickname ${account.nickname} not found", "NOT_FOUND_ACCOUNT") }

        val commentLike = CommentLike(comment, loginUser.id!!)
        if (commentLikeRepository.existsByCommentAndAccountId(comment, loginUser.id)) {
            throw ApiException(ErrorReason.CONFLICT, "Comment like already exists for comment id $commentId and account id ${loginUser.id}", "ALREADY_LIKED_COMMENT")
        }

        commentLikeRepository.save(commentLike)

        commentMetaRepository.findByComment(comment)
            .ifPresentOrElse(
                { it.likeCount += 1 },
                { commentMetaRepository.save(CommentMeta(comment, 1)) }
            )
    }
}
