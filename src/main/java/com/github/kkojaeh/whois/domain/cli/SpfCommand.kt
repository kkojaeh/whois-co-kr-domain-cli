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
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SPF(TXT) record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--spf"], description = ["SPF(TXT) record value"], required = true)
  var spf: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.addSpf(host, spf)
    domainModel.save(connection, model)
    return 0
  }

}

@CommandLine.Command(
  name = "delete-spf",
  description = ["delete SPF(TXT) record"],
  mixinStandardHelpOptions = true
)
class DeleteSpfCommand : Callable<Int> {

  @CommandLine.Mixin
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SPF(TXT) record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--spf"], description = ["SPF(TXT) record value"])
  var spf: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.deleteSpf(host, spf)
    domainModel.save(connection, model)
    return 0
  }

}