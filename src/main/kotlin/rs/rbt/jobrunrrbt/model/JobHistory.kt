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

/*"@class": "org.jobrunr.jobs.states.ScheduledState",
      "state": "SCHEDULED",
      "createdAt": "2023-01-18T13:45:11.798865Z",
      "scheduledAt": "2023-01-18T16:45:11.796683Z",
      "recurringJobId": null,
      "reason": null*/