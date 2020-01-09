package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-mx",
  description = ["add MX record"],
  mixinStandardHelpOptions = true
)
class AddMxCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["MX record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--mx"], description = ["MX record mx server value"], required = true)
  var mx: String = ""

  @CommandLine.Option(names = ["--priority"], description = ["MX record priority value"], required = true)
  var priority: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.addMx(host, mx, priority)
    return domainCommand.save(model)
  }

}

@CommandLine.Command(
  name = "delete-mx",
  description = ["delete MX record"],
  mixinStandardHelpOptions = true
)
class DeleteMxCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["spf record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--mx"], description = ["MX record mx server value"], required = true)
  var mx: String = ""

  @CommandLine.Option(names = ["--priority"], description = ["MX record priority value"])
  var priority: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.deleteMx(host, mx, priority)
    return domainCommand.save(model)
  }

}
