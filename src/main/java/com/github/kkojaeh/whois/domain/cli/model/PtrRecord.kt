package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap

class PtrRecord(
  override var index: Int,
  var id: String,
  var host: String,
  var ptr: String
) : Record by RecordImpl() {

  fun toMap(): LinkedMultiValueMap<String, String> {
    val map = LinkedMultiValueMap<String, String>()
    map.add("ptr_rec_id_" + index, id);
    if (add) {
      map.add("ptr_action_type_" + index, "A");
      map.add("ptr_new_host_" + index, host);
      map.add("ptr_new_ptr_" + index, ptr);
    } else if (delete) {
      map.add("ptr_action_type_" + index, "D");
      map.add("ptr_cur_host_" + index, host);
      map.add("ptr_cur_ptr_" + index, ptr);
    } else {
      map.add("ptr_new_host_" + index, host);
      map.add("ptr_cur_host_" + index, host);
      map.add("ptr_cur_ptr_" + index, ptr);
    }
    return map
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as PtrRecord

    if (index != other.index) return false
    if (host != other.host) return false
    if (ptr != other.ptr) return false

    return true
  }

  override fun hashCode(): Int {
    var result = index
    result = 31 * result + host.hashCode()
    result = 31 * result + ptr.hashCode()
    return result
  }

  companion object {

    fun from(domain: String, map: LinkedMultiValueMap<String, String>): List<PtrRecord> {
      val count = Integer.parseInt(map.getFirst("rec_cnt_ptr"))
      val result = mutableListOf<PtrRecord>()
      for (i in 0 until count) {
        val record = PtrRecord(
          i + 1,
          map.get("ptr_rec_id")?.get(i) ?: "",
          resolveHost(map.get("ptr")?.get(i) ?: "", domain),
          resolveHost(map.get("ptr_host")?.get(i) ?: "", "in-addr.arpa")
        )
        result.add(record)
      }
      return result;
    }

    fun toMap(records: List<PtrRecord>): LinkedMultiValueMap<String, String> {
      val map = LinkedMultiValueMap<String, String>()
      records.forEach { record ->
        map.addAll(record.toMap())
      }
      map.add("rec_cnt_ptr", "" + records.size)
      map.add("req_cnt_ptr", "" + records.count { record -> record.isRequest() })
      return map
    }
  }
}
