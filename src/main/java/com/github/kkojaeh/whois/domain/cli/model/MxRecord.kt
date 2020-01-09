package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class MxRecord(
  override var index: Int,
  var id: String,
  var host: String,
  var priority: String,
  var mx: String
) : Record by RecordImpl() {

  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("mx_rec_id_" + index, id);
    if (add) {
      map.add("mx_action_type_" + index, "A");
      map.add("mx_new_host_" + index, host);
      map.add("mx_new_mx_" + index, mx);
      map.add("mx_new_priority_" + index, priority);
    } else if (delete) {
      map.add("mx_action_type_" + index, "D");
      map.add("mx_cur_host_" + index, host);
      map.add("mx_cur_mx_" + index, mx);
      map.add("mx_cur_priority_" + index, priority);
    } else {
      map.add("mx_new_host_" + index, host);
      map.add("mx_cur_host_" + index, host);
      map.add("mx_cur_mx_" + index, mx);
      map.add("mx_cur_priority_" + index, priority);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MxRecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (priority != other.priority) return false
    if (mx != other.mx) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + priority.hashCode()
    result = 31 * result + mx.hashCode()
    return result
  }


  companion object {

    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<MxRecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_mx"))
      val result = mutableListOf<MxRecord>()
      for (i in 0 until count) {
        val record = MxRecord(
          i + 1,
          map.get("mx_rec_id")?.get(i) ?: "",
          resolveHost(map.get("mail_host")?.get(i) ?: "", domain),
          map.get("priority")?.get(i) ?: "",
          map.get("mx")?.get(i) ?: ""
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<MxRecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_mx", "" + records.size)
      map.add("req_cnt_mx", "" + records.count { record -> record.isRequest() })
      return map
    }
  }
}
