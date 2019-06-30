package cn.dmp.tags

import ch.hsr.geohash.GeoHash
import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.Row
import redis.clients.jedis.Jedis

object Tags4business extends Tags {
    /**
      * 打标签的方法定义
      *
      * @param args
      * @return
      */
    override def makeTags(args: Any*): Map[String, Int] = {
        var map = Map[String,Int]()
        val row = args(0).asInstanceOf[Row]
        val jedis = args(1).asInstanceOf[Jedis]

        val lat = row.getAs[String]("lat")
        val longs = row.getAs[String]("long")

        if (StringUtils.isNotEmpty(lat) && StringUtils.isEmpty(longs)){

            val lat2 = lat.toDouble
            val long2 = lat.toDouble
            if (lat2 > 3 && lat2 < 54 && long2 >73 && long2 < 136 ){
                val geoHashCode = GeoHash.withCharacterPrecision(lat2,long2,8).toBase32
                val business = jedis.get(geoHashCode)
                if (StringUtils.isNotEmpty(business))
                    business.split(",").foreach(bs => map += "BS" + bs ->1)
            }

        }

        map
    }
}
