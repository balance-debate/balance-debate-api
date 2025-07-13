package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Debate

data class DebateGetResponse(
    val topic: String,
    val choiceA: String,
    val choiceB: String,
) {
    companion object {
        fun from(debates: List<Debate>): List<DebateGetResponse> {
            return debates.map { from(it) }
        }

        fun from(debate: Debate): DebateGetResponse {
            return DebateGetResponse(
                topic = debate.topic,
                choiceA = debate.choiceA,
                choiceB = debate.choiceB
            )
        }
    }
}