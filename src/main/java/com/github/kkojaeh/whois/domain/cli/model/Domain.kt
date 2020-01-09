package com.github.kkojaeh.whois.domain.cli.model

import org.springframework.util.LinkedMultiValueMap


class Domain(val domain: String, val map: LinkedMultiValueMap<String, String>) {

  private val mxRecords = mutableListOf<MxRecord>()

  private val spfRecords = mutableListOf<SpfRecord>()

  private val aRecords = mutableListOf<ARecord>()

  private val cnameRecords = mutableListOf<CnameRecord>()

  private val ptrRecords = mutableListOf<PtrRecord>()

  private val srvRecords = mutableListOf<SrvRecord>()

  init {
    mxRecords.addAll(MxRecord.from(domain, map))
    spfRecords.addAll(SpfRecord.from(domain, map))
    aRecords.addAll(ARecord.from(domain, map))
    cnameRecords.addAll(CnameRecord.from(domain, map))
    ptrRecords.addAll(PtrRecord.from(domain, map))
    srvRecords.addAll(SrvRecord.from(domain, map))
  }

  fun addSpf(host: String, spf: String) {
    val record = SpfRecord(
      spfRecords.size + 1,
      "",
      host,
      spf
    )
    record.add()
    spfRecords.add(record)
  }

  fun deleteSpf(host: String, spf: String) {
    var sequence = spfRecords.asSequence()
    sequence = sequence.filter { record -> record.host == host }
    if (spf.isNotEmpty()) {
      sequence = sequence.filter { record -> record.spf == spf }
    }
    sequence.forEach { record -> record.delete() }
  }

  fun addMx(host: String, mx: String, priority: String) {
    val record = MxRecord(
      mxRecords.size + 1,
      "",
      host,
      priority,
      mx
    )
    record.add()
    mxRecords.add(record)
  }

  fun deleteMx(host: String, mx: String, priority: String) {
    var sequence = mxRecords.asSequence()
    sequence = sequence.filter { record -> record.mx == mx }
    if (host.isNotEmpty()) {
      sequence = sequence.filter { record -> record.host == host }
    }
    if (priority.isNotEmpty()) {
      sequence = sequence.filter { record -> record.priority == priority }
    }
    sequence.forEach { record -> record.delete() }
  }

  fun addA(host: String, ip: String) {
    val record = ARecord(
      aRecords.size + 1,
      "",
      host,
      ip
    )
    record.add()
    aRecords.add(record)
  }

  fun deleteA(host: String) {
    aRecords
      .filter { record -> record.host == host }
      .forEach { record -> record.delete() }
  }

  fun addCname(host: String, cname: String) {
    val record = CnameRecord(
      cnameRecords.size + 1,
      "",
      host,
      cname
    )
    record.add()
    cnameRecords.add(record)
  }

  fun deleteCname(host: String, cname: String) {
    var sequence = cnameRecords.asSequence()
    sequence = sequence.filter { record -> record.host == host }
    if (cname.isNotEmpty()) {
      sequence = sequence.filter { record -> record.cname == cname }
    }
    sequence.forEach { record -> record.delete() }
  }

  fun addPtr(host: String, ptr: String) {
    val record = PtrRecord(
      ptrRecords.size + 1,
      "",
      host,
      ptr
    )
    record.add()
    ptrRecords.add(record)
  }

  fun deletePtr(host: String, ptr: String) {
    var sequence = ptrRecords.asSequence()
    sequence = sequence.filter { record -> record.ptr == ptr }
    if (host.isNotEmpty()) {
      sequence = sequence.filter { record -> record.host == host }
    }
    sequence.forEach { record -> record.delete() }
  }

  fun addSrv(host: String, srv: String) {
    val record = SrvRecord(
      srvRecords.size + 1,
      "",
      host,
      srv
    )
    record.add()
    srvRecords.add(record)
  }

  fun deleteSrv(host: String, srv: String) {
    var sequence = srvRecords.asSequence()
    sequence = sequence.filter { record -> record.srv == srv }
    if (host.isNotEmpty()) {
      sequence = sequence.filter { record -> record.host == host }
    }
    sequence.forEach { record -> record.delete() }
  }

  fun hasRequest(): Boolean {
    val requestCount = sequenceOf(mxRecords, spfRecords, aRecords, cnameRecords, ptrRecords, srvRecords)
      .map { list -> list.count { record -> record.isRequest() } }
      .sum()
    return requestCount > 0
  }

  fun toMap(): LinkedMultiValueMap<String, String> {
    val result = LinkedMultiValueMap<String, String>()
    result.addAll(MxRecord.toMap(mxRecords))
    result.addAll(SpfRecord.toMap(spfRecords))
    result.addAll(ARecord.toMap(aRecords))
    result.addAll(CnameRecord.toMap(cnameRecords))
    result.addAll(PtrRecord.toMap(ptrRecords))
    result.addAll(SrvRecord.toMap(srvRecords))

    result.add("sel_domain", domain)
    result.add("sel_enc_domain", domain)
    result.add("sel_domain_box", domain)
    result.add("threadid[]", "threadID_0")
    result.add("domain_id", map.getFirst("domain_id"))

    result.add("onepage", "D")
    result.add("service_info", "")
    result.add("dns_cnt", "0")
    result.add("use_add_service", "")
    result.add("basic_service", "N")
    result.add("domains[]", "");
    result.add("list_domains[]", domain);
    return result
  }

  fun apply() {
    sequenceOf(mxRecords, spfRecords, aRecords, cnameRecords, ptrRecords, srvRecords)
      .forEach { records ->
        records.removeIf { record -> record.delete }
        records.forEachIndexed { index, record -> record.index = index + 1 }
      }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Domain

    if (domain != other.domain) return false
    if (mxRecords != other.mxRecords) return false
    if (spfRecords != other.spfRecords) return false
    if (aRecords != other.aRecords) return false
    if (cnameRecords != other.cnameRecords) return false
    if (ptrRecords != other.ptrRecords) return false
    if (srvRecords != other.srvRecords) return false

    return true
  }

  override fun hashCode(): Int {
    var result = domain.hashCode()
    result = 31 * result + mxRecords.hashCode()
    result = 31 * result + spfRecords.hashCode()
    result = 31 * result + aRecords.hashCode()
    result = 31 * result + cnameRecords.hashCode()
    result = 31 * result + ptrRecords.hashCode()
    result = 31 * result + srvRecords.hashCode()
    return result
  }


}

/*
code=[1000]
msg=[성공적으로 완료 되었습니다.]
domain=[togle.io]
domain_type=[io]
domain_name=[togle]
threadID=[threadID_0]
encode_domain=[togle.io]
dns_msg=[success to PDNS(List)!!]
onepage=[D]
domain_id=[886456]
rec_cnt=[3]
dns_cnt=[0]
basic_service=[N]
ns_type=[whois]
ns1_host=[ns1.whoisdomain.kr]
ns2_host=[ns2.whoisdomain.kr]
ns3_host=[ns3.whoisdomain.kr]
ns4_host=[ns4.whoisdomain.kr]
cnt=[14]
rec_cnt_a=[3]
a_rec_id=[7005207, 7005208, 7070590]
a_host=[www.togle.io, dev.togle.io, sentry.togle.io]
ip=[61.251.171.94, 61.251.171.109, 61.251.171.148]
rec_cnt_cname=[3]
cname_rec_id=[7005254, 7005255, 7005256]
cname_host=[nxquipim6bsiwjj7d4gxi63cctrrjczc._domainkey.togle.io, zyasfi2k6bj4xipruh2crjmhnro2b5j2._domainkey.togle.io, tfo4lxo4smjkjuh2ee6v36orw3okhfa6._domainkey.togle.io]
cname=[nxquipim6bsiwjj7d4gxi63cctrrjczc.dkim.amazonses.com, zyasfi2k6bj4xipruh2crjmhnro2b5j2.dkim.amazonses.com, tfo4lxo4smjkjuh2ee6v36orw3okhfa6.dkim.amazonses.com]
rec_cnt_mx=[2]
mx_rec_id=[7005332, 7005333]
mail_host=[togle.io, togle.io]
priority=[10, 20]
mx=[ASPMX.daum.net, ALT.ASPMX.daum.net]
rec_cnt_spf=[6]
spf_rec_id=[7005252, 7005689, 7108908, 7109067, 7109941, 7110151]
spf_host=[_amazonses.togle.io, _acme-challenge.togle.io, abcd.togle.io, dddd.togle.io, abc.togle.io, kjh.togle.io]
spf=[4Hh666CyuXDK9biAH4pMBBW6F/+7oEiN4Rhy8cynuEgequal, 9A3YMIomATJcLtw4PnHiy68Zs2xf_sIZrBmGjcenFNM, efg, dddd, zzzz, kkojaeh]
rec_cnt_ptr=[0]
rec_cnt_srv=[0]
rec_cnt_ptr=[1]
ptr_rec_id=[7110559]
ptr_host=[56.34.23.12.in-addr.arpa]
ptr=[ptr.togle.io]
rec_cnt_srv=[1]
srv_rec_id=[7110560]
srv_host=[srv.togle.io]
srv=[srv-value]
 */