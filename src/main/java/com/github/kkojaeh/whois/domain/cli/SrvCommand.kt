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
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SRV record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--srv"], description = ["SRV record SRV value"], required = true)
  var srv: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.addSrv(host, srv)
    return domainCommand.save(model)
  }

}

@CommandLine.Command(
  name = "delete-srv",
  description = ["delete SRV record"],
  mixinStandardHelpOptions = true
)
class DeleteSrvCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["SRV record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--srv"], description = ["SRV record SRV value"], required = true)
  var srv: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.deleteSrv(host, srv)
    return domainCommand.save(model)
  }

}
