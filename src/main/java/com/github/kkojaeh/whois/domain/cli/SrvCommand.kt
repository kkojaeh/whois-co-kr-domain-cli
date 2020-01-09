package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-srv",
  description = ["add SRV record"],
  mixinStandardHelpOptions = true
)
class AddSrvCommand : Callable<Int> {

  @CommandLine.Mixin
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SRV record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--srv"], description = ["SRV record SRV value"], required = true)
  var srv: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.addSrv(host, srv)
    domainModel.save(connection, model)
    return 0
  }

}

@CommandLine.Command(
  name = "delete-srv",
  description = ["delete SRV record"],
  mixinStandardHelpOptions = true
)
class DeleteSrvCommand : Callable<Int> {

  @CommandLine.Mixin
  val authenticate = AuthenticateCommand()
  @CommandLine.Mixin
  val domainModel = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SRV record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--srv"], description = ["SRV record SRV value"], required = true)
  var srv: String = ""

  override fun call(): Int {
    val connection = authenticate.login()
    val model = domainModel.model(connection, authenticate.username)
    model.deleteSrv(host, srv)
    domainModel.save(connection, model)
    return 0
  }

}