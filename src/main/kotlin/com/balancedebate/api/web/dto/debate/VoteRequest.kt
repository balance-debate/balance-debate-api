package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.VoteTarget

data class VoteRequest(
    val target: VoteTarget,
)
