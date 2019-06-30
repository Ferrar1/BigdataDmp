package cn.dmp.utils

import org.apache.spark.sql.Row

import scala.collection.mutable.ListBuffer

object TagsUtils {
    val hasSomeUserIdCondition =
        """
          |imei != "" or imeimd5 != "" or imeisha1 != "" or
          |idfa != "" or idfamd5 != "" or idfasha1 != "" or
          |mac != "" or macmd5 != "" or macsha1 != "" or
          |androidid != "" or androididmd5 != "" or androididsha1 != "" or
          |openudid != "" or openudidmd5 != "" or openudidsha1 != ""
        """.stripMargin

    val testCondition =
        """
          |(imei != "" or imeimd5 != "" or imeisha1 != "" or
          |idfa != "" or idfamd5 != "" or idfasha1 != "" or
          |mac != "" or macmd5 != "" or macsha1 != "" or
          |androidid != "" or androididmd5 != "" or androididsha1 != "" or
          |openudid != "" or openudidmd5 != "" or openudidsha1 != "") and
          |lat > 3 and lat < 54 and long >73 and long < 136
        """.stripMargin

    def getAllUserId(v: Row): ListBuffer[String] = {
        val userIds = new collection.mutable.ListBuffer[String]()


        if (v.getAs[String]("imei").nonEmpty) userIds.append("IM:" + v.getAs[String]("imei").toUpperCase)
        if (v.getAs[String]("idfa").nonEmpty) userIds.append("DF:" + v.getAs[String]("idfa").toUpperCase)
        if (v.getAs[String]("mac").nonEmpty) userIds.append("MC:" + v.getAs[String]("mac").toUpperCase)
        if (v.getAs[String]("androidid").nonEmpty) userIds.append("AD:" + v.getAs[String]("androidid").toUpperCase)
        if (v.getAs[String]("openudid").nonEmpty) userIds.append("OU:" + v.getAs[String]("openudid").toUpperCase)

        if (v.getAs[String]("imeimd5").nonEmpty) userIds.append("IMM:" + v.getAs[String]("imeimd5").toUpperCase)
        if (v.getAs[String]("idfamd5").nonEmpty) userIds.append("DFM:" + v.getAs[String]("idfamd5").toUpperCase)
        if (v.getAs[String]("macmd5").nonEmpty) userIds.append("MCM:" + v.getAs[String]("macmd5").toUpperCase)
        if (v.getAs[String]("androididmd5").nonEmpty) userIds.append("ADM:" + v.getAs[String]("androididmd5").toUpperCase)
        if (v.getAs[String]("openudidmd5").nonEmpty) userIds.append("OUM:" + v.getAs[String]("openudidmd5").toUpperCase)

        if (v.getAs[String]("imeisha1").nonEmpty) userIds.append("IMS:" + v.getAs[String]("imeisha1").toUpperCase)
        if (v.getAs[String]("idfasha1").nonEmpty) userIds.append("DFS:" + v.getAs[String]("idfasha1").toUpperCase)
        if (v.getAs[String]("macsha1").nonEmpty) userIds.append("MCS:" + v.getAs[String]("macsha1").toUpperCase)
        if (v.getAs[String]("androididsha1").nonEmpty) userIds.append("ADS:" + v.getAs[String]("androididsha1").toUpperCase)
        if (v.getAs[String]("openudidsha1").nonEmpty) userIds.append("OUS:" + v.getAs[String]("openudidsha1").toUpperCase)


        userIds
    }
}
