package rs.rbt.jobrunrrbt.model

class JobDetails (
    var className: String,
    var staticFieldName: String?,
    var methodName: String,
    var jobParameters: JobParameters,
    var cacheable: Boolean,
)



/*"className": "org.jobrunr.examples.services.MyServiceInterface",
    "staticFieldName": null,
    "methodName": "doSimpleJob",
    "jobParameters": [
      {
        "className": "java.lang.String",
        "actualClassName": "java.lang.String",
        "object": "Hello world"
      }
    ],
    "cacheable": true*/