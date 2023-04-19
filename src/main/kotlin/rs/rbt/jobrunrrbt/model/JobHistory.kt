package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.AT_CLASS
import rs.rbt.jobrunrrbt.helper.CustomOffsetDateTimeDeserializer
import rs.rbt.jobrunrrbt.helper.PATTERN_SSSSSSX
import rs.rbt.jobrunrrbt.helper.PATTERN_SSSSSSZ
import java.time.LocalDateTime
import java.time.OffsetDateTime

/**  `JobHistory` is a class that represents a job history record */
@JsonIgnoreProperties(ignoreUnknown = true)
class JobHistory(
    @JsonProperty(AT_CLASS)
    var atClass: String,
    var state: String,
    @DateTimeFormat(style = PATTERN_SSSSSSZ)
    @JsonFormat(pattern = PATTERN_SSSSSSZ)
    var createdAt: LocalDateTime,
    @DateTimeFormat(style = PATTERN_SSSSSSZ)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN_SSSSSSX)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @field:JsonDeserialize(using = CustomOffsetDateTimeDeserializer::class)
    var scheduledAt: OffsetDateTime?,
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    var recurringJobId: String?,
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    var reason: String?,
)