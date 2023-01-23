package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonProperty

class JobParameters (

    var className: String,
    var actualClassName: String,
    @JsonProperty("object")
    var jobObject: String,



)

/* "className": "java.lang.String",
        "actualClassName": "java.lang.String",
        "object": "Hello world"*/