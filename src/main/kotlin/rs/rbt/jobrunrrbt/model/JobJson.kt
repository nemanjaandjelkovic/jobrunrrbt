package rs.rbt.jobrunrrbt.model


class JobJson (

    var version: Long,
    var jobSignature: String,
    var jobName: String,
    var jobDetails: JobDetails,
    var id: String,
    var jobHistory: JobHistory,

)