package rs.rbt.jobrunrrbt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class JobrunrrbtApplication


fun main(args: Array<String>) {
	runApplication<JobrunrrbtApplication>(*args)
}
