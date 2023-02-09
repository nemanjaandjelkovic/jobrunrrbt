package rs.rbt.jobrunrrbt

import jakarta.annotation.PostConstruct
import org.jobrunr.scheduling.JobScheduler
import org.springframework.stereotype.Component

@Component
class JobFactory(private val scheduler: JobScheduler) {

    @PostConstruct
    fun schedule(){
        scheduler.enqueue{
            println("Hello World")
        }
    }

}