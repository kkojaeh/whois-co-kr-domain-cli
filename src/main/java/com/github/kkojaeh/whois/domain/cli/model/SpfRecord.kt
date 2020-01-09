package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class SpfRecord(
  override var index: Int,
  var id: String,
  var host: String,
  var spf: String
) : Record by RecordImpl() {

  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("spf_rec_id_" + index, id);
    if (add) {
      map.add("spf_action_type_" + index, "A");
      map.add("spf_new_host_" + index, host);
      map.add("spf_new_spf_" + index, spf);
    } else if (delete) {
      map.add("spf_action_type_" + index, "D");
      map.add("spf_cur_host_" + index, host);
      map.add("spf_cur_spf_" + index, spf);
    } else {
      map.add("spf_new_host_" + index, host);
      map.add("spf_cur_host_" + index, host);
      map.add("spf_cur_spf_" + index, spf);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as SpfRecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (spf != other.spf) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + spf.hashCode()
    return result
  }


  companion object {

    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<SpfRecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_spf"))
      val result = mutableListOf<SpfRecord>()
      for (i in 0 until count) {
        val record = SpfRecord(
          i + 1,
          map.get("spf_rec_id")?.get(i) ?: "",
          resolveHost(map.get("spf_host")?.get(i) ?: "", domain),
          map.get("spf")?.get(i) ?: ""
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<SpfRecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_spf", "" + records.size)
      map.add("req_cnt_spf", "" + records.count { record -> record.isRequest() })
      return map
    }
  }
}
