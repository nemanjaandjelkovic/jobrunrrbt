package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.DATE_TIME_STYLE
import rs.rbt.jobrunrrbt.helper.State
import java.time.LocalDateTime

class JobHistory (
    @JsonProperty("@class")
    var atClass: String,
    var state: State,
    @DateTimeFormat(style = DATE_TIME_STYLE)
    @JsonFormat(pattern = DATE_TIME_STYLE)
    var createdAt: LocalDateTime,
    @DateTimeFormat(style = DATE_TIME_STYLE)
    @JsonFormat(pattern = DATE_TIME_STYLE)
    var scheduledAt: LocalDateTime,
    var recurringJobId: String?,
    var reason: String?,

    )