package rs.rbt.jobrunrrbt.model

/** A Kotlin class that holds valid job states. */
enum class JobState {

    SCHEDULED,
    ENQUEUED,
    PROCESSING,
    FAILED,
    SUCCEEDED,
    DELETED
}