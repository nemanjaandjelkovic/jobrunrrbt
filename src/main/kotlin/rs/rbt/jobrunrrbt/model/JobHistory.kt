package rs.rbt.jobrunrrbt.model

import rs.rbt.jobrunrrbt.helper.State
import java.time.LocalDateTime

class JobHistory (
    var atClass: String,
    var state: State,
    var createdAt: LocalDateTime,
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