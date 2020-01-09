package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-spf",
  description = ["add SPF(TXT) record"],
  mixinStandardHelpOptions = true
)
class AddSpfCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SPF(TXT) record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--spf"], description = ["SPF(TXT) record value"], required = true)
  var spf: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.addSpf(host, spf)
    return domainCommand.save(model)
  }

}

@CommandLine.Command(
  name = "delete-spf",
  description = ["delete SPF(TXT) record"],
  mixinStandardHelpOptions = true
)
class DeleteSpfCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SPF(TXT) record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--spf"], description = ["SPF(TXT) record value"])
  var spf: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.deleteSpf(host, spf)
    return domainCommand.save(model)
  }

}
