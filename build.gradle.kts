import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import groovy.time.TimeCategory
import java.util.Date

plugins {
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.dokka") version "1.6.10"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
	jacoco
    //kotlin("plugin.serialization") version "1.6.10"
	//id("io.github.chiragji.jacotura") version "1.0.2"
}

group = "ovh.studywithme"
version = "0.0.7"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	//Development
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	//Documentation
	dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")

	//Runtime
	runtimeOnly("mysql:mysql-connector-java")

	//Testing
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
        //exclude(module = "junit")
        exclude(module = "mockito-core")
    }
	testImplementation("org.springframework.security:spring-security-test")
	//testImplementation("io.mockk:mockk:1.12.3")
	testImplementation("com.ninja-squad:springmockk:3.1.1")
	testImplementation("com.h2database:h2")
    //testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation( "com.beust:klaxon:5.5")
	//testImplementation( "org.junit.jupiter:junit-jupiter-api:5.8.2")
	//testImplementation( "org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

//tasks.withType<Test> {
//	useJUnitPlatform()
//}
tasks.test {
	useJUnitPlatform {
        includeTags("integration")
        excludeTags("repository")
    }
	//finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

//jacotura {
//	properties {
//	  property("jacotura.jacoco.path", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
//	  property("jacotura.cobertura.path", "$buildDir/reports/cobertura.xml")
//	}
	//sourceDirs = sourceSets.main.kotlin.srcDirs
//}

var testResults by extra(mutableListOf<TestOutcome>()) // Container for tests summaries

tasks.withType<Test>().configureEach {
    val testTask = this

    testLogging {
        events = setOf(
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED,
            TestLogEvent.STANDARD_OUT,
            TestLogEvent.STANDARD_ERROR
        )

        exceptionFormat = TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }

    ignoreFailures = false // Always try to run all tests for all modules

    //addTestListener is a workaround https://github.com/gradle/kotlin-dsl-samples/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
            if (desc.parent != null) return // Only summarize results for whole modules

            val summary = TestOutcome().apply {
                add( "${testTask.project.name}:${testTask.name} results: ${result.resultType} " +
                        "(" +
                        "${result.testCount} tests, " +
                        "${result.successfulTestCount} successes, " +
                        "${result.failedTestCount} failures, " +
                        "${result.skippedTestCount} skipped" +
                        ") " +
                        "in ${TimeCategory.minus(Date(result.endTime), Date(result.startTime))}")
                add("Report file: ${testTask.reports.html.entryPoint}")
            }

            // Add reports in `testsResults`, keep failed suites at the end
            if (result.resultType == TestResult.ResultType.SUCCESS) {
                testResults.add(0, summary)
            } else {
                testResults.add(summary)
            }
        }
    })
}

gradle.buildFinished {
    if (testResults.isNotEmpty()) {
        printResults(testResults)
    }
}

fun printResults(allResults:List<TestOutcome>) {
    val maxLength = allResults.map{ it.maxWidth() }
        .max() ?: 0

    println("┌${"─".repeat(maxLength)}┐")

    println(allResults.joinToString("├${"─".repeat(maxLength)}┤\n") { testOutcome ->
        testOutcome.lines.joinToString("│\n│", "│", "│") {
            it + " ".repeat(maxLength - it.length)
        }
    })

    println("└${"─".repeat(maxLength)}┘")
}

data class TestOutcome(val lines:MutableList<String> = mutableListOf()) {
    fun add(line:String) {
        lines.add(line)
    }

    fun maxWidth(): Int {
        return lines.maxBy { it.length }?.length ?: 0
    }
}
