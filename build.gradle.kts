import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
  id("org.springframework.boot") version "2.2.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  kotlin("jvm") version "1.3.61"
  kotlin("plugin.spring") version "1.3.61"
  id("com.jfrog.bintray") version "1.7.3"
  id("maven")
  id("maven-publish")
  id("ch.netzwerg.release") version "1.2.3"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test") {
    exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
  }
  implementation("info.picocli:picocli:4.1.4")
  implementation("org.jsoup:jsoup:1.12.1")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

val sourcesJar by tasks.creating(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").allSource)
}


publishing {
  publications {
    create<MavenPublication>("bintray") {
      groupId = project.group as String?
      artifactId = project.name
      version = project.version as String?
      from(components["java"])

      pom.withXml {
        asNode().apply {
          appendNode("description", "https://github.com/kkojaeh/whois-co-kr-domain-cli")
          appendNode("name", rootProject.name)
          appendNode("url", "https://github.com/kkojaeh/whois-co-kr-domain-cli")
          appendNode("licenses").appendNode("license").apply {
            appendNode("name", "MIT")
            appendNode("url", "https://opensource.org/licenses/mit-license.php")
            appendNode("distribution", "repo")
          }
          appendNode("developers").appendNode("developer").apply {
            appendNode("id", "kkojaeh")
            appendNode("name", "Jaehun Ko")
          }
          appendNode("scm").apply {
            appendNode("url", "https://github.com/kkojaeh/whois-co-kr-domain-cli")
          }
        }
      }
    }
  }
}

bintray {
  user = project.findProperty("bintray.username").toString()
  key = project.findProperty("bintray.api.key").toString()
  publish = true

  setPublications("bintray")

  pkg.apply {
    repo = "maven"
    name = project.name
    userOrg = user
    githubRepo = githubRepo
    vcsUrl = "https://github.com/kkojaeh/whois-co-kr-domain-cli"
    description = "Port of ruby faker gem written in kotlin"
    setLabels("kotlin", "faker", "testing", "test-automation", "data", "generation")
    setLicenses("MIT")
    desc = description
    websiteUrl = "https://github.com/kkojaeh/whois-co-kr-domain-cli"
    issueTrackerUrl = "https://github.com/kkojaeh/whois-co-kr-domain-cli/issues"
    githubReleaseNotesFile = "README.md"

    version.apply {
      name = project.name
      desc = "https://github.com/kkojaeh/whois-co-kr-domain-cli"
      released = Date().toString()
      vcsTag = project.version as String?
    }
  }
}

release {
  dependsOn(tasks.build)
  push = true
  versionSuffix = "-SNAPSHOT"
  tagPrefix = "v"
}
