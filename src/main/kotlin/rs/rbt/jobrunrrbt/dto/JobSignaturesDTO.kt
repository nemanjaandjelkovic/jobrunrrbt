package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The above code defines a data class JobSignaturesDTO with a list property containing JobSignatureDTO
 * objects, using the Jackson annotation @JsonProperty.
 * @property {List<JobSignatureDTO>} list - `list` is a property of type `List<JobSignatureDTO>`. It is
 * a collection of `JobSignatureDTO` objects.
 */
data class JobSignaturesDTO(
    @JsonProperty()
    val list: List<JobSignatureDTO>
)
