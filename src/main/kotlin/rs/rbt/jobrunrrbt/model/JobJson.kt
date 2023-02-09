package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class JobJson (

    var version: Long,
    var jobSignature: String,
    var jobName: String,
    var amountOfRetries: Int?,
    var labels: List<String?>,
    var jobDetails: JobDetails,
    var id: String,
    var jobHistory: Any,
    var metadata: Any,

)