package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository

interface VoteRepository : JpaRepository<Vote, Long> {
    fun findByDebateIdAndAccountId(debateId: Long, accountId: String): Vote?
}
