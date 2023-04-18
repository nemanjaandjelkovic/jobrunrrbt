package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.AT_CLASS
import rs.rbt.jobrunrrbt.helper.CustomOffsetDateTimeDeserializer
import rs.rbt.jobrunrrbt.helper.DATE_TIME_STYLE
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

/**  `JobHistory` is a class that represents a job history record */
@JsonIgnoreProperties(ignoreUnknown = true)
class JobHistory (
    @JsonProperty(AT_CLASS)
    var atClass: String,
    var state: String,
    @DateTimeFormat(style = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'")
    var createdAt: LocalDateTime,
    @DateTimeFormat(style = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSX")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @field:JsonDeserialize(using = CustomOffsetDateTimeDeserializer::class)
    var scheduledAt: OffsetDateTime?,
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    var recurringJobId: String?,
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    var reason: String?,
    )