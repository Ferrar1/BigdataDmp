package cn.dmp.tags

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row
import students.jialiang.StringUtil

object Tags4Ads extends  Tags {
    override def makeTags(args: Any*): Map[String, Int] = {
        var map = Map[String,Int]()

        val row = args(0).asInstanceOf[Row]
        //广告位类型和名称
        val adTypeId = row.getAs[Int]("adspacetype")
        val adTypeName = row.getAs[String]("adspacetypename")

        if(adTypeId > 9) map += "LC"+adTypeId -> 1
        else if(adTypeId > 0) map += "LC0"+adTypeId -> 1

        if (StringUtils.isNotEmpty(adTypeName)) map += "LN"+adTypeName -> 1

        //渠道的
        val chanelId = row.getAs[Int]("adplatformproviderid")
        if(chanelId > 0) map += (("CN"+chanelId,1))

        map
    }
}
