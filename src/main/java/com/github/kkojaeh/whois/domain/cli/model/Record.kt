package com.github.kkojaeh.whois.domain.cli.model

interface Record {
  var index: Int
  var add: Boolean
  var delete: Boolean
  fun isRequest(): Boolean
  fun add()
  fun delete()
}

class RecordImpl : Record {

  override var index: Int = 0

  override var add = false

  override var delete = false

  override fun isRequest(): Boolean {
    return add || delete
  }

  override fun add() {
    add = true
  }

  override fun delete() {
    delete = true
  }

}

fun resolveHost(value: String, domain: String): String {
  val regexp = "\\.?" + (domain.replace(".", "\\.")) + "$"
  return value.replace(regexp.toRegex(), "")
}