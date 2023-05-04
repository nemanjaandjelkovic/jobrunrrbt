package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonProperty
import rs.rbt.jobrunrrbt.helper.ARG_DATA
import rs.rbt.jobrunrrbt.helper.ARG_ROW

/**
 * This is a data class in Kotlin that represents job arguments with an integer row and an optional
 * string data.
 * @property {Int} argRow - argRow is an integer property that represents a row number.
 * @property {String?} argData - argData is a nullable String property of the JobArgumentsDTO data
 * class. It represents some data that is associated with a job. The "?" after the String type
 * indicates that this property can be null.
 */
data class JobArgumentsDTO(
    @JsonProperty(ARG_ROW)
    val argRow: Int,
    @JsonProperty(ARG_DATA)
    val argData: String?
)