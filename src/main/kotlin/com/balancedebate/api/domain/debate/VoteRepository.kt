package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository

interface VoteRepository : JpaRepository<Vote, Long> {
    fun findByDebateIdAndUuid(debateId: Long, uuid: String): Vote?

    fun findByDebateIdAndAccountId(debateId: Long, accountId: Long): Vote?
}
