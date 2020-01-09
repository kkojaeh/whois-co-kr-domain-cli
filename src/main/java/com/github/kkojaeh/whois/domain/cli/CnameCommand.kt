package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-cname",
  description = ["add CNAME record"],
  mixinStandardHelpOptions = true
)
class AddCnameCommand : Callable<Int> {

  @CommandLine.Mixin
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["CNAME record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--cname"], description = ["CNAME record CNAME value"], required = true)
  var cname: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.addCname(host, cname)
    domainModel.save(connection, model)
    return 0
  }

}

@CommandLine.Command(
  name = "delete-cname",
  description = ["delete CNAME record"],
  mixinStandardHelpOptions = true
)
class DeleteCnameCommand : Callable<Int> {

  @CommandLine.Mixin
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["CNAME record domain prefix"], required = true)
  var host: String = ""

  @CommandLine.Option(names = ["--cname"], description = ["CNAME record CNAME value"])
  var cname: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.deleteCname(host, cname)
    domainModel.save(connection, model)
    return 0
  }

}