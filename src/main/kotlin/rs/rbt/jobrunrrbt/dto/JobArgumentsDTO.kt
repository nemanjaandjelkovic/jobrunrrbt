package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty

class JobArgumentsDTO (
    @JsonProperty("argRow")
    val argRow: Int,
    @JsonProperty("argData")
    val argData: String?
    )