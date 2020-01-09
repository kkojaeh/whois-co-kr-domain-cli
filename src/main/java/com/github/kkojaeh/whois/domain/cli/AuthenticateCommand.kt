package com.github.kkojaeh.whois.domain.cli;

import org.jsoup.Connection
import org.jsoup.Jsoup
import picocli.CommandLine


@CommandLine.Command(synopsisHeading = "%nUsage:%n%n",
  descriptionHeading = "%nDescription:%n%n",
  parameterListHeading = "%nParameters:%n%n",
  optionListHeading = "%nOptions:%n%n",
  commandListHeading = "%nCommands:%n%n")
open class AuthenticateCommand {

  @CommandLine.Option(names = ["-u", "--username"], description = ["username1"], required = true)
  var username: String = ""

  @CommandLine.Option(names = ["-p", "--password"], description = ["p@ssword"], required = true)
  var password: String = ""

  private val loginPageUrl = "https://member.whois.co.kr/sso/index.php?tpf=login"

  private val loginProcessUrl = "https://member.whois.co.kr/sso/?tpf=login_process"


  fun decorate(connection: Connection) {
    val request = connection.request()
    request.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
      .header("Accept-Encoding", "gzip, deflate, br")
      .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
      .header("Cache-Control", "max-age=0")
      .header("Connection", " keep-alive")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Origin", "https://domain.whois.co.kr")
      .header("Referer", "https://domain.whois.co.kr/ns_service/service.php")
      .header("Sec-Fetch-Mode", "navigate")
      .header("Sec-Fetch-Site", "same-origin")
      .header("Sec-Fetch-User", "?1")
      .header("Upgrade-Insecure-Requests", "1")
  }


  fun login(): Connection {
    if (username.isEmpty() || password.isEmpty()) {
      println("username and password required")
    }
    val connection = Jsoup.connect(loginPageUrl)
    decorate(connection)
    val doc = connection.get()
    val form = doc.selectFirst("form[name=form_sso_social]")
    val formData = mutableMapOf<String, String?>()
    form.select("input").forEach { input ->
      formData.put(input.attr("name"), input.attr("value"))
    }
    formData.put("whois_sso_id", username)
    formData.put("whois_sso_pwd", password)

    connection.url(loginProcessUrl)
      .data(formData)
      .ignoreContentType(true)
      .post()

    connection.request().data().clear()

    return connection
  }

}
