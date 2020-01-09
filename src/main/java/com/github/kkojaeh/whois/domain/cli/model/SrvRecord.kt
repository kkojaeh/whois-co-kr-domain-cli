package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class SrvRecord(
  override var index: Int,
  var id: String,
  var host: String,
  var srv: String
) : Record by RecordImpl() {

  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("srv_rec_id_" + index, id);
    if (add) {
      map.add("srv_action_type_" + index, "A");
      map.add("srv_new_host_" + index, host);
      map.add("srv_new_srv_" + index, srv);
    } else if (delete) {
      map.add("srv_action_type_" + index, "D");
      map.add("srv_cur_host_" + index, host);
      map.add("srv_cur_srv_" + index, srv);
    } else {
      map.add("srv_new_host_" + index, host);
      map.add("srv_cur_host_" + index, host);
      map.add("srv_cur_srv_" + index, srv);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as SrvRecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (srv != other.srv) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + srv.hashCode()
    return result
  }

  companion object {


    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<SrvRecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_srv"))
      val result = mutableListOf<SrvRecord>()
      for (i in 0 until count) {
        val record = SrvRecord(
          i + 1,
          map.get("srv_rec_id")?.get(i) ?: "",
          resolveHost(map.get("srv_host")?.get(i) ?: "", domain),
          map.get("srv")?.get(i) ?: ""
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<SrvRecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_srv", "" + records.size)
      map.add("req_cnt_srv", "" + records.count { record -> record.isRequest() })
      return map
    }
  }
}