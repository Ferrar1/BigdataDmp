package cn.dmp.tools

import cn.dmp.beans.Log
import cn.dmp.utils.JedisPools
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 将字典库存入到redis中
  */
object AppDict2Redis {

    def main(args: Array[String]): Unit = {

        // 0 校验参数个数
        if (args.length != 1) {
            println(
                """
                  |cn.dmp.tools.AppDict2Redis
                  |参数：
                  | appdictInputPath
                """.stripMargin)
            sys.exit()
        }

        // 1 接受程序参数
        val Array(appdictInputPath) = args

        // 2 创建sparkconf->sparkContext
        val sparkConf = new SparkConf()
        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
        sparkConf.setMaster("local[*]")
        // RDD 序列化到磁盘 worker与worker之间的数据传输
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

        val sc = new SparkContext(sparkConf)

        sc.textFile(appdictInputPath).map(line => {
            val fields = line.split("\t", -1)
            (fields(4), fields(1))
        }).foreachPartition(itr => {

            val jedis = JedisPools.getJedis()

            itr.foreach(t => {
                jedis.set(t._1, t._2)
            })

            jedis.close()
        })


        sc.stop()
    }

}
