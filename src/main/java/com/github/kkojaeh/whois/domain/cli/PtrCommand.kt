package com.github.kkojaeh.whois.domain.cli

import picocli.CommandLine
import java.util.concurrent.Callable

@CommandLine.Command(
  name = "add-ptr",
  description = ["add PTR record"],
  mixinStandardHelpOptions = true
)
class AddPtrCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["PTR record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--ptr"], description = ["PTR record PTR(ip) value"], required = true)
  var ptr: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.addPtr(host, ptr)
    return domainCommand.save(model)
  }

}

@CommandLine.Command(
  name = "delete-ptr",
  description = ["delete PTR record"],
  mixinStandardHelpOptions = true
)
class DeletePtrCommand : Callable<Int> {

  @CommandLine.Mixin
  val domainCommand = DomainCommand()

  @CommandLine.Option(names = ["--host"], description = ["PTR record domain prefix"])
  var host: String = ""

  @CommandLine.Option(names = ["--ptr"], description = ["PTR record PTR(ip) value"], required = true)
  var ptr: String = ""

  override fun call(): Int {
    val model = domainCommand.model()
    model.deletePtr(host, ptr)
    return domainCommand.save(model)
  }

}
