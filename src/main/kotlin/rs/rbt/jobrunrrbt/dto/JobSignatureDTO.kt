package rs.rbt.jobrunrrbt.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.jobrunr.utils.mapper.jackson.modules.InstantDeserializer
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.DATE_TIME_STYLE
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

class JobSignatureDTO(
    @JsonProperty("jobSignature")
    val jobSignature: String,
    @JsonProperty("jobArg")
    val jobArguments: Array<JobArgumentsDTO>,
    @JsonProperty("jobDate")
    @DateTimeFormat(style = DATE_TIME_STYLE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    val jobTime: OffsetDateTime
)
