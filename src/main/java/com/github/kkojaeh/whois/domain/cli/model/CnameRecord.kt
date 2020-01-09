package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class CnameRecord(
  override var index: Int,
  var id: String,
  var host: String,
  var cname: String
) : Record by RecordImpl() {

  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("cname_rec_id_" + index, id);
    if (add) {
      map.add("cname_action_type_" + index, "A");
      map.add("cname_new_host_" + index, host);
      map.add("cname_new_cname_" + index, cname);
    } else if (delete) {
      map.add("cname_action_type_" + index, "D");
      map.add("cname_cur_host_" + index, host);
      map.add("cname_cur_cname_" + index, cname);
    } else {
      map.add("cname_new_host_" + index, host);
      map.add("cname_cur_host_" + index, host);
      map.add("cname_cur_cname_" + index, cname);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as CnameRecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (cname != other.cname) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + cname.hashCode()
    return result
  }

  companion object {

    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<CnameRecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_cname"))
      val result = mutableListOf<CnameRecord>()
      for (i in 0 until count) {
        val record = CnameRecord(
          i + 1,
          map.get("cname_rec_id")?.get(i) ?: "",
          resolveHost(map.get("cname_host")?.get(i) ?: "", domain),
          map.get("cname")?.get(i) ?: ""
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<CnameRecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_cname", "" + records.size)
      map.add("req_cnt_cname", "" + records.count { record -> record.isRequest() })
      return map
    }
  }


}
