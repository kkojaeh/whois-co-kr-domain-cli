package com.github.kkojaeh.whois.domain.cli

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine
import java.util.concurrent.Callable


@CommandLine.Command(
  subcommands = [
    AddSpfCommand::class,
    DeleteSpfCommand::class,
    AddMxCommand::class,
    DeleteMxCommand::class,
    AddACommand::class,
    DeleteACommand::class,
    AddCnameCommand::class,
    DeleteCnameCommand::class,
    AddPtrCommand::class,
    DeletePtrCommand::class,
    AddSrvCommand::class,
    DeleteSrvCommand::class
  ]
)
@SpringBootApplication
class Application : CommandLineRunner, Callable<Int> {
  override fun run(vararg args: String) {
    val exitCode: Int = CommandLine(this).execute(*args)
    System.exit(exitCode)
  }

  override fun call(): Int {
    println("http://whois.co.kr domain record changer")
    return 0
  }
}

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}