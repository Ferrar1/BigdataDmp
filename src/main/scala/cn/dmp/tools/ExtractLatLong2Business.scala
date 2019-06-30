package cn.dmp.tools

import ch.hsr.geohash.GeoHash
import cn.dmp.utils.{BaiduGeoApi, JedisPools}
import org.apache.commons.lang.StringUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 用来抽取日志字段中的经纬度，并请求百度的api获取到商圈信息
*/

object ExtractLatLong2Business {
    def main(args: Array[String]): Unit = {

        // 0 校验参数个数
        if (args.length != 1) {
            println(
                """
                  | cn.dmp.tools.ExtractLatLong2Business
                  |参数：
                  | inputPath
                """.stripMargin)
            sys.exit()
        }

        // 1 接受程序参数
        val Array(inputPath) = args

        // 2 创建sparkconf->sparkContext
        val sparkConf = new SparkConf()
        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
        sparkConf.setMaster("local[*]")
        // RDD 序列化到磁盘 worker与worker之间的数据传输
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

        val sc = new SparkContext(sparkConf)
        val sQLContext = new SQLContext(sc)

        sQLContext.read.parquet(inputPath)
                .select("lat","long")
                .where("lat > 3 and lat < 54 and long >73 and long < 136")
                .distinct()
                .foreachPartition(itr => {
                    //jedis客户端
                    val jedis = JedisPools.getJedis()
                    itr.foreach(row => {
                        val lat = row.getAs[String]("lat")
                        val long = row.getAs[String]("long")

                        val geoHashCode = GeoHash.withCharacterPrecision(lat.toDouble, long.toDouble,8).toBase32
                        val business = BaiduGeoApi.getBusiness(lat+","+long)

                        if (StringUtils.isNotEmpty(business) )
                            jedis.set(geoHashCode,business)

                    })
                    jedis.close()

                })

        sc.stop()
    }
}
