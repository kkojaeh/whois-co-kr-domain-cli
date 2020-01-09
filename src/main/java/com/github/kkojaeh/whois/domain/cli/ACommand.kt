package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-a",
  description = ["add A record"],
  mixinStandardHelpOptions = true
)
class AddACommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["A record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--ip"], description = ["A record ip value"], required = true)
  var ip: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.addA(host, ip)
    return domainCommand.save(model)
  }

}

@CommandLine.Command(
  name = "delete-a",
  description = ["delete A record"],
  mixinStandardHelpOptions = true
)
class DeleteACommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["A record domain prefix"], required = true)
  var host: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.deleteA(host)
    return domainCommand.save(model)
  }

}
