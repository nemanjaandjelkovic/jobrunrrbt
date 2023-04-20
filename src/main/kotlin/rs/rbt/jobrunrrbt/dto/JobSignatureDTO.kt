package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.*
import java.time.OffsetDateTime

class JobSignatureDTO(
    @JsonProperty(JOB_SIGNATURE_CAMEL_CASE)
    val jobSignature: String,
    @JsonProperty(JOB_ARGUMENTS)
    val jobArguments: Array<JobArgumentsDTO>,
    @JsonProperty(JOB_DATE)
    @DateTimeFormat(style = PATTERN_SSSSSSZ)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS[S][S][S][S][S][S]XXX")
    val jobTime: OffsetDateTime
)
