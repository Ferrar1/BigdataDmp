package students.yangyang

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

object TerritorySQL {
  def main(args: Array[String]): Unit = {
    //1.准备工作(效验参数个数)
    if (args.length != 1) {
      println(
        """|Territory
           |logInputPath
        """.stripMargin)
      sys.exit()
    }
    //    2.接收程序的参数
    val Array(logInputPath) = args

    // 创建spark配置，应用程序名称以及线程数量     local[*]在本地进行调试
    val conf: SparkConf = new SparkConf()
      .setAppName("TerritorySQL")
      .setMaster("local[*]")
    //创建RDD入口
    val sc: SparkContext = new SparkContext(conf)
    //构建一个SQLContext
    val scsql: SQLContext = new SQLContext(sc)
    //    获取文件路径
    //val parquetFile: DataFrame = scsql.read.format("parquet").load(logInputPath)
    val JsontFile: DataFrame = scsql.read.format("json").load(logInputPath)
    JsontFile.registerTempTable("JsontFile")

    // 原始请求数
    val d1: DataFrame = scsql.sql("select provincename, cityname, count(*) ct1  from JsontFile where requestmode=1 and processnode >=1 GROUP BY provincename,cityname")
    d1.registerTempTable("PrimaryRequest")
    //d1.show()

    //有效请求数
    val d2: DataFrame = scsql.sql("select provincename, cityname, count(*) ct2 from JsontFile where requestmode=1 and processnode >=2 GROUP BY provincename,cityname")
    d2.registerTempTable("ValidRequest")
    //d2.show()

    // 广告请求数
    val d3: DataFrame = scsql.sql("select provincename, cityname, count(*) ct3  from JsontFile where requestmode=1 and processnode >=3 GROUP BY provincename,cityname")
    d3.registerTempTable("AdRequest")
    //d3.show()

    //参与竞价数
    val d4: DataFrame = scsql.sql("select provincename, cityname, count(*) ct4   from JsontFile where iseffective=1 and isbilling =1 and isbid = 1 and adorderid !=0 GROUP BY provincename,cityname")
    d4.registerTempTable("Bidding")
    //d4.show()

    //竞价成功数.
    val d5: DataFrame = scsql.sql("select provincename, cityname, count(*) ct5  from JsontFile where iseffective=1 and isbilling =1 and iswin = 1 GROUP BY provincename,cityname")
    d5.registerTempTable("BiddingWin")
    //d5.show()

    //展示数
    val d6: DataFrame = scsql.sql("select provincename, cityname, count(*) ct6  from JsontFile where requestmode=1  and iseffective=1 GROUP BY provincename,cityname")
    d6.registerTempTable("DisplayAdvertising")
    //d6.show()

    //    点击数
    val d7: DataFrame = scsql.sql("select provincename, cityname, count(*) ct7  from JsontFile where requestmode=3  and iseffective=1 GROUP BY provincename,cityname")
    d7.registerTempTable("Hits")
    //d7.show()

    //DSP广告消费
    val d8: DataFrame = scsql.sql("select provincename, cityname, count(*) ct8 from JsontFile where iseffective=1 and isbilling =1 and iswin = 1 GROUP BY provincename,cityname")
    d8.registerTempTable("DSPConsume")
    //d8.show()

    //    DSP广告成本
    val d9: DataFrame = scsql.sql("select provincename, cityname, count(*)  ct9  from JsontFile where iseffective=1 and isbilling =1 and iswin = 1 GROUP BY provincename,cityname")
    d9.registerTempTable("DSPCost")
    //d9.show()


    //    把多表统一成一张表（LEFT JOIN）
    val sqltext: DataFrame = scsql.sql("SELECT t1.* , t2.ct2 FROM PrimaryRequest as t1 LEFT JOIN ValidRequest AS t2  ON t1.cityname =t2.cityname")
      sqltext.registerTempTable("to1")
    // sql.show()
    val sqltext1: DataFrame = scsql.sql("SELECT t1.* , t2.ct3 FROM to1 as t1 LEFT JOIN AdRequest AS t2  ON t1.cityname =t2.cityname")
    sqltext1.registerTempTable("to2")
    //sqltext.show()
    val sqltext2: DataFrame = scsql.sql("SELECT t1.* , t2.ct4 FROM to2 as t1 LEFT JOIN Bidding AS t2  ON t1.cityname =t2.cityname")
    sqltext2.registerTempTable("to3")
    //sqltext2.show()
    val sqltext3: DataFrame = scsql.sql("SELECT t1.* , t2.ct5 FROM to3 as t1 LEFT JOIN BiddingWin AS t2  ON t1.cityname =t2.cityname")
    sqltext3.registerTempTable("to4")
    //sqltext3.show()
    val sqltext4: DataFrame = scsql.sql("SELECT t1.* , t2.ct6 FROM to4 as t1 LEFT JOIN DisplayAdvertising AS t2  ON t1.cityname =t2.cityname")
    sqltext4.registerTempTable("to5")
    //sqltext4.show()
    val sqltext5: DataFrame = scsql.sql("SELECT t1.* , t2.ct7 FROM to5 as t1 LEFT JOIN Hits AS t2  ON t1.cityname =t2.cityname")
    sqltext5.registerTempTable("to6")
    // sqltext5.show()
    val sqltext6: DataFrame = scsql.sql("SELECT t1.* , t2.ct8 FROM to6 as t1 LEFT JOIN DSPConsume AS t2  ON t1.cityname =t2.cityname")
    sqltext6.registerTempTable("to7")
    //sqltext6.show()
    val sqltext7: DataFrame = scsql.sql("SELECT t1.* , t2.ct9 FROM to7 as t1 LEFT JOIN DSPCost AS t2  ON t1.cityname =t2.cityname")
    sqltext7.registerTempTable("to8")

    //把null填充为0
    val map: Map[String, Int] = Map("provincename" -> 0
      , "cityname" -> 0
      , "ct1" -> 0
      , "ct2" -> 0
      , "ct3" -> 0
      , "ct4" -> 0
      , "ct5" -> 0
      , "ct6" -> 0
      , "ct7" -> 0
      , "ct8" -> 0
      , "ct9" -> 0)
    val fill: DataFrame = sqltext7.na.fill(map)
    //fill.registerTempTable("to9")
    //fill.show()


    //把数据写入Mysql数据库中（jdbc）
    val properties: Properties = new Properties()
    properties.put("user", "root")
    properties.put("password", "123456")
    sqltext7.write.jdbc("jdbc:mysql://localhost:3306/sys?characterEncoding=UTF-8", "adv_area_count_null", properties)

    sc.stop()


  }

}
