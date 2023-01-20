package rs.rbt.jobrunrrbt.model


class JobJson (

    var version: Long,
    var jobSignature: String,
    var jobName: String,
    var jobDetails: JobDetails,
    var id: String,
    var jobHistory: JobHistory,
    //metadata


)
 /* {
  "version": 1,
  "jobSignature": "org.jobrunr.examples.services.MyServiceInterface.doSimpleJob(java.lang.String)",
  "jobName": "org.jobrunr.examples.services.MyServiceInterface.doSimpleJob(Hello world)",
  "jobDetails": {
    "className": "org.jobrunr.examples.services.MyServiceInterface",
    "staticFieldName": null,
    "methodName": "doSimpleJob",
    "jobParameters": [
      {
        "className": "java.lang.String",
        "actualClassName": "java.lang.String",
        "object": "Hello world"
      }
    ],
    "cacheable": true
  },
  "id": "761fcf5c-ea57-49e2-8101-63eb9a59fdbb",
  "jobHistory": [
    {
      "@class": "org.jobrunr.jobs.states.ScheduledState",
      "state": "SCHEDULED",
      "createdAt": "2023-01-18T13:45:11.798865Z",
      "scheduledAt": "2023-01-18T16:45:11.796683Z",
      "recurringJobId": null,
      "reason": null
    }
  ],
  "metadata": {
    "@class": "java.util.concurrent.ConcurrentHashMap"
  }
}*/