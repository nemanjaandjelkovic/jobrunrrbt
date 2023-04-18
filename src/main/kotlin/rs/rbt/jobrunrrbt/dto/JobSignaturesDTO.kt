package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JobSignaturesDTO(
    @JsonProperty()
    val list: List<JobSignatureDTO>
)
