package com.github.kkojaeh.whois.domain.cli

import com.github.kkojaeh.whois.domain.cli.model.Domain
import org.jsoup.Connection
import org.springframework.util.LinkedMultiValueMap
import picocli.CommandLine

@CommandLine.Command(synopsisHeading = "%nUsage:%n%n",
  descriptionHeading = "%nDescription:%n%n",
  parameterListHeading = "%nParameters:%n%n",
  optionListHeading = "%nOptions:%n%n",
  commandListHeading = "%nCommands:%n%n")
open class DomainCommand {

  private val pageUrl = "https://domain.whois.co.kr/ns_service/service.php"

  private val confirmUrl = "https://domain.whois.co.kr/ns_service/service_confirm.php"

  private val infoUrl = "https://domain.whois.co.kr/ns_service/service_info.php"

  private val actionUrl = "https://domain.whois.co.kr/ns_service/service_action.php"

  private val doneUrl = "https://domain.whois.co.kr/ns_service/service_done.php"

  @CommandLine.Option(names = ["-d", "--domain"], description = ["google.com"], required = true)
  var domain: String = ""

  fun model(connection: Connection, username: String): Domain {
    connection.url(pageUrl)
      .get()
    val txt = connection.url(infoUrl)
      .data("domain", domain)
      .data("member", username)
      .data("threadID", "threadID_0")
      .post()
      .text()

    val info = LinkedMultiValueMap<String, String>();
    txt.split("|||").forEach {
      val split = it.split("=")
      val name = split[0]
      val values = split[1]
      values.split("|")
        .filter { it -> it.isNotEmpty() }
        .forEach { value -> info.add(name, value) }
    }
    return Domain(domain, info)
  }

  fun save(connection: Connection, model: Domain) {
    if (!model.hasRequest()) {
      return
    }
    connection.request().data().clear()
    model.toMap().forEach { name, values ->
      values.forEach { value ->
        connection.data(name, value)
      }
    }
    val confirmedPage = connection.url(confirmUrl)
      .post()
    val confirmedForm = confirmedPage.selectFirst("form")
    val actionData = LinkedMultiValueMap<String, String>();
    confirmedForm.select("input").forEach { input ->
      actionData.add(input.attr("name"), input.attr("value"))
    }
    connection.request().data().clear()
    actionData.forEach { name, values ->
      values.forEach { value ->
        connection.data(name, value)
      }
    }
    val actionedPage = connection.url(actionUrl)
      .post()

    val actionedForm = actionedPage.selectFirst("form")
    val confirmData = mutableMapOf<String, String?>()
    actionedForm.select("input").forEach { input ->
      confirmData.put(input.attr("name"), input.attr("value"))
    }
    connection.url(doneUrl)
      .data(confirmData)
      .post()

    model.apply()
  }

}


