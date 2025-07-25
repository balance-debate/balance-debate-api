package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.debate.Debate
import org.springframework.data.domain.Slice

data class DebateSliceResponse(
    val hasNext: Boolean,
    val debates: List<DebateGetResponse>,
) {
    companion object {
        fun from(debates: Slice<Debate>): DebateSliceResponse {
            return DebateSliceResponse(
                hasNext = debates.hasNext(),
                debates = DebateGetResponse.from(debates.content)
            )
        }
    }
}
