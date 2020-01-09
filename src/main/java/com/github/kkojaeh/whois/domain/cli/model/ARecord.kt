package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class ARecord(
  override var index: Int,
  var id: String,
  var host: String,
  var ip: String
) : Record by RecordImpl() {


  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("a_rec_id_" + index, id);
    if (add) {
      map.add("a_action_type_" + index, "A");
      map.add("a_new_host_" + index, host);
      map.add("a_new_a_" + index, ip);
    } else if (delete) {
      map.add("a_action_type_" + index, "D");
      map.add("a_cur_host_" + index, host);
      map.add("a_cur_a_" + index, ip);
    } else {
      map.add("a_cur_host_" + index, host);
      map.add("a_cur_a_" + index, ip);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ARecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (ip != other.ip) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + ip.hashCode()
    return result
  }

  companion object {

    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<ARecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_a"))
      val result = mutableListOf<ARecord>()
      for (i in 0 until count) {
        val record = ARecord(
          i + 1,
          map.get("a_rec_id")?.get(i) ?: "",
          resolveHost(map.get("a_host")?.get(i) ?: "", domain),
          map.get("ip")?.get(i) ?: ""
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<ARecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_a", "" + records.size)
      map.add("req_cnt_a", "" + records.count { record -> record.isRequest() })
      return map
    }
  }

}
