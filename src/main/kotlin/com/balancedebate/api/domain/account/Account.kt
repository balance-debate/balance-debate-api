package com.balancedebate.api.domain.account

import com.balancedebate.api.domain.BaseEntity
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import jakarta.persistence.*
import java.io.Serializable

@Entity
class Account private constructor(
    @Column(unique = true, nullable = false)
    val nickname: String,

    @Column(nullable = false)
    private val password: String,

    @Column(nullable = false)
    val profileEmoji: String,
) : BaseEntity(), Serializable {

    constructor() : this("", "", "")

    fun isCorrectPassword(password: String): Boolean {
        return this.password == PasswordCryptoUtil.encrypt(password)
    }
    companion object {
        fun of(nickname: String, password: String, profileEmoji: String): Account {
            if (nickname.isEmpty()) {
                throw ApiException(errorReason = ErrorReason.BAD_REQUEST, message = "닉네임은 1글자 이상이어야 합니다.", code = "NOT_ALLOWED_EMPTY_NICKNAME")
            }

            return Account(nickname, PasswordCryptoUtil.encrypt(password), profileEmoji)
        }
    }
}
