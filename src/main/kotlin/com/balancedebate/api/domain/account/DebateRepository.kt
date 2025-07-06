package com.balancedebate.api.domain.account

import org.springframework.data.jpa.repository.JpaRepository

interface DebateRepository : JpaRepository<Debate, Long>
