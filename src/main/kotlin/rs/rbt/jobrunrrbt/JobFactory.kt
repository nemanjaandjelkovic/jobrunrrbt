//package rs.rbt.jobrunrrbt
//
//import jakarta.annotation.PostConstruct
//import org.jobrunr.jobs.annotations.Job
//import org.jobrunr.scheduling.JobScheduler
//import org.springframework.stereotype.Component
//import java.time.LocalDateTime
//
//@Component
//class JobFactory(private val scheduler: JobScheduler) {
//
//    @PostConstruct
//    @Job(name = "1 minute delay job")
//    fun schedule() {
//        scheduler.schedule(LocalDateTime.now().plusMinutes(1)) { println("Job with 1 minute delay") }
//    }
//
//}