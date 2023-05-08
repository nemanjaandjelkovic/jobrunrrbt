package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

/**  It's a Kotlin data class that represents a job in the queue */
@JsonIgnoreProperties(ignoreUnknown = true)
data class JobJson(
    var version: Long,
    var jobSignature: String,
    var jobName: String,
    var amountOfRetries: Int?,
    var labels: List<String?>,
    var jobDetails: JobDetails,
    var id: String,
    var jobHistory: ArrayList<JobHistory> = arrayListOf(),
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var metadata: Any?,
)