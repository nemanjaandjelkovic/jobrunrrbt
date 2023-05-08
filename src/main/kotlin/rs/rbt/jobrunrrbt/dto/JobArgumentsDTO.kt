package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty
import rs.rbt.jobrunrrbt.helper.ARG_DATA
import rs.rbt.jobrunrrbt.helper.ARG_ROW

data class JobArgumentsDTO(
    @JsonProperty(ARG_ROW)
    val argRow: Int,
    @JsonProperty(ARG_DATA)
    val argData: String?
)