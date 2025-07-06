package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.account.Debate

data class DebateListGetResponse(
    val topic: String,
    val choiceA: String,
    val choiceB: String,
) {
    companion object {
        fun from(debates: List<Debate>): List<DebateListGetResponse> {
            return debates.map { from(it) }
        }

        private fun from(debate: Debate): DebateListGetResponse {
            return DebateListGetResponse(
                topic = debate.topic,
                choiceA = debate.choiceA,
                choiceB = debate.choiceB
            )
        }
    }
}