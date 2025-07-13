package com.balancedebate.api.domain.debate

import org.springframework.data.jpa.repository.JpaRepository

interface DebateRepository : JpaRepository<Debate, Long>
