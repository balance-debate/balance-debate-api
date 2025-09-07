package com.balancedebate.api.domain.account

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository : JpaRepository<Account, Long> {

    fun findByNickname(nickname: String): Optional<Account>

    fun findByIdIn(accountIds: List<Long>): List<Account>
}
