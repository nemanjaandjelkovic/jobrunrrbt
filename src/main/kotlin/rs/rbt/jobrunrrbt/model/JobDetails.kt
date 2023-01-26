package rs.rbt.jobrunrrbt.model

class JobDetails (
    var className: String,
    var staticFieldName: String?,
    var methodName: String,
    var jobParameters: List<JobParameters>,
    var cacheable: Boolean,

)
