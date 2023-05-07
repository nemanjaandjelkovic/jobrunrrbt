package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import rs.rbt.jobrunrrbt.helper.CLASS_NAME
import rs.rbt.jobrunrrbt.helper.METHOD_NAME
import rs.rbt.jobrunrrbt.helper.PACKAGE_NAME
import rs.rbt.jobrunrrbt.helper.SCHEDULED_TIME
import java.time.OffsetDateTime

class UpdateJobReceivedDTO(
    @JsonProperty(PACKAGE_NAME)
    val packageName: String,
    @JsonProperty(METHOD_NAME)
    val methodName: String,
    @JsonProperty(CLASS_NAME)
    val className: String,
    @JsonProperty(SCHEDULED_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[S][S][S][S][S][S]XXX")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheduledTime: OffsetDateTime?
)