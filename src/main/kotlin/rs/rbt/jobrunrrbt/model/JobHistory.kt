package rs.rbt.jobrunrrbt.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.format.annotation.DateTimeFormat
import rs.rbt.jobrunrrbt.helper.State
import java.time.LocalDateTime

class JobHistory (
    @JsonProperty("@class")
    var atClass: String,
    var state: State,
    @DateTimeFormat(style = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    var createdAt: LocalDateTime,
    @DateTimeFormat(style = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    var scheduledAt: LocalDateTime,
    var recurringJobId: String?,
    var reason: String?,

    )