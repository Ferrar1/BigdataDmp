package cn.dmp.report

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 利用spark算子的方式进行离线数据统计 ---- 各省市的数据量分布情况
  */
object ProCityRptV3 {

    def main(args: Array[String]): Unit = {

        // 0 校验参数个数
        if (args.length != 2) {
            println(
                """
                  |cn.dmp.report.ProCityRptV3
                  |参数：
                  | logInputPath
                  | resultOutputPath
                """.stripMargin)
            sys.exit()
        }

        // 1 接受程序参数
        val Array(logInputPath, resultOutputPath) = args

        // 2 创建sparkconf->sparkContext
        val sparkConf = new SparkConf()
        sparkConf.setAppName(s"${this.getClass.getSimpleName}")
        sparkConf.setMaster("local[*]")
        // RDD 序列化到磁盘 worker与worker之间的数据传输
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

        val sc = new SparkContext(sparkConf)

        // 读取数据进行统计
        sc.textFile(logInputPath)
          .map(line => line.split(",", -1)).filter(_.length >= 85)
          // (河北省|石家庄市， 1)  ((河北省，石家庄市), 1)
          .map(arr => ((arr(24), arr(25)), 1))
          .reduceByKey(_ + _)
          .map(t => t._1._1+","+t._1._2+","+t._2)
          .saveAsTextFile(resultOutputPath)

        sc.stop()
    }

}
