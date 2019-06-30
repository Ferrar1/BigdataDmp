package cn.dmp.tags

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

object Tags4device extends Tags {
    override def makeTags(args: Any*): Map[String, Int] = {
        var map = Map[String,Int]()

        val row = args(0).asInstanceOf[Row]

        val os = row.getAs[Int]("client")
        val phoneType = row.getAs[String]("device")
        val ntm = row.getAs[String]("networkmannername")
        val ispname = row.getAs[String]("ispname")

        os match  {
            case 1 => map += "D000100001" -> 1
            case 2 => map += "D000100002" -> 1
            case 3 => map += "D000100003" -> 1
            case _ => map += "D000100004" -> 1
        }
        if(StringUtils.isEmpty(phoneType)) map += "DN" + phoneType -> 1

        ntm.toUpperCase match  {
            case "WIFI" => map += "D000300001" -> 1
            case "4G" => map += "D000300002" -> 1
            case "3G" => map += "D000300003" -> 1
            case "2G" => map += "D000300004" -> 1
            case _ => map += "D000300005" -> 1
        }

        ispname match  {
            case "移动" => map += "D000400001" -> 1
            case "联通" => map += "D000400002" -> 1
            case "电信" => map += "D000400003" -> 1
            case _ => map += "D000400004" -> 1
        }

        val pName = row.getAs[String]("provincename")
        val cName = row.getAs[String]("cityname")

        if(StringUtils.isEmpty(pName)) map += "ZP" + pName -> 1
        if(StringUtils.isEmpty(pName)) map += "ZC" + cName -> 1


        map

    }
}
