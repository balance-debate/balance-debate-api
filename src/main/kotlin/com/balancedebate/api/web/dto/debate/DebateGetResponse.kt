package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Debate
import java.time.LocalDateTime

data class DebateGetResponse(
    val topic: String,
    val choiceA: String,
    val choiceB: String,
    val thumbnailUrl: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(debates: List<Debate>): List<DebateGetResponse> {
            return debates.map { from(it) }
        }

        fun from(debate: Debate): DebateGetResponse {
            return DebateGetResponse(
                topic = debate.topic,
                choiceA = debate.choiceA,
                choiceB = debate.choiceB,
                thumbnailUrl = debate.thumbnailUrl,
                createdAt = debate.createdAt,
                updatedAt = debate.updatedAt,
            )
        }
    }
}