package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonProperty
import rs.rbt.jobrunrrbt.helper.OBJECT

/**  The JobParameters class is a data class that holds the class name, the actual class name, and the
job object. */
data class JobParameters(
    var className: String,
    var actualClassName: String,
    @JsonProperty(OBJECT)
    var jobObject: String?,
) {
    override fun toString(): String {
        return actualClassName
    }
}