package rs.rbt.jobrunrrbt.model

/**  It's a class that holds the details of a job */
data class JobDetails(
    var className: String,
    var staticFieldName: String?,
    var methodName: String,
    var jobParameters: List<JobParameters>,
    var cacheable: Boolean,
)
