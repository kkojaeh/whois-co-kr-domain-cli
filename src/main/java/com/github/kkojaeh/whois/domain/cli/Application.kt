package com.github.kkojaeh.whois.domain.cli

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import picocli.CommandLine
import java.util.concurrent.Callable


@CommandLine.Command(
  customSynopsis = ["java -jar whois-co-kr-domain-cli.jar [command] [options]"],
  description = ["후이즈(http://whois.co.kr) 도메인의 레코드를 변경합니다.", "각 [command] 와 --help 옵션을 이용하여 실행 옵션을 확인하세요", "ex) java -jar whois-co-kr-domain-cli.jar add-spf --help"],
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
    println(CommandLine(this).usageMessage)
    return 0
  }
}

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
