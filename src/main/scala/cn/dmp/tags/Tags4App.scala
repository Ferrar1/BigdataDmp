package cn.dmp.tags

import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row

object Tags4App extends Tags {
    override def makeTags(args: Any*): Map[String, Int] = {
        var map = Map[String,Int]()

        val row = args(0).asInstanceOf[Row]
        val appDict = args(1).asInstanceOf[Map[String,String]]

        val appId = row.getAs[String]("appid")
        val appName = row.getAs[String]("appname")

        if(StringUtils.isEmpty(appName)){
            appDict.contains(appId) match {
                case true => map += "APP" + appDict.get(appId) -> 1

            }
        }
        map
    }
}
