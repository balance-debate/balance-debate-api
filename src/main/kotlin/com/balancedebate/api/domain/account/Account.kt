package com.balancedebate.api.domain.account

import com.balancedebate.api.domain.BaseEntity
import jakarta.persistence.*
import java.io.Serializable

@Entity
class Account private constructor(
    @Column(unique = true, nullable = false)
    val nickname: String,

    @Column(nullable = false)
    private val password: String,
) : BaseEntity(), Serializable {

    protected constructor() : this("", "")

    fun isCorrectPassword(password: String): Boolean {
        return this.password == PasswordCryptoUtil.encrypt(password)
    }
    companion object {
        fun of(nickname: String, password: String): Account {
            require(nickname.isNotEmpty()) { "닉네임은 1글자 이상이어야 합니다." }
            return Account(nickname, PasswordCryptoUtil.encrypt(password))
        }
    }
}
