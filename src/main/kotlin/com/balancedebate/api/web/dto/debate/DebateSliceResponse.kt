package com.balancedebate.api.web.dto.debate

import com.balancedebate.api.domain.account.Debate
import org.springframework.data.domain.Slice

data class DebateSliceResponse(
    val hasNext: Boolean,
    val debates: List<DebateListGetResponse>,
) {
    companion object {
        fun from(debates: Slice<Debate>): DebateSliceResponse {
            return DebateSliceResponse(
                hasNext = debates.hasNext(),
                debates = DebateListGetResponse.from(debates.content)
            )
        }
    }
}
