package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonProperty
import rs.rbt.jobrunrrbt.helper.OBJECT

class JobParameters (

    var className: String,
    var actualClassName: String,
    @JsonProperty(OBJECT)
    var jobObject: String,

)