package ovh.studywithme.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Study with me server application
 *
 * @constructor Create empty Study with me server application
 */
@SpringBootApplication
class StudyWithMeServerApplication

/**
 * Main
 *
 * @param args
 */
fun main(args: Array<String>) {
	runApplication<StudyWithMeServerApplication>(*args)
}
