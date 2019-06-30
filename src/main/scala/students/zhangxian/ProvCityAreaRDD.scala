package students.zhangxian

import java.util.Properties

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

object ProvCityAreaRDD {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("logToParquet").setMaster("local[*]")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    // 读取parquet为DataFrame
    val data: DataFrame = sqlContext.read.parquet("E:/mrdata/ad/logs/parquet/")
    val rowRDD: RDD[Row] = data.rdd

    val provAndCityAndParas = rowRDD.map(t => {
      val ysqqs = if (t(8) == 1 && t(35).asInstanceOf[Int] >= 1) 1 else 0
      val yxqqs = if (t(8) == 1 && t(35).asInstanceOf[Int] >= 2) 1 else 0
      val ggqqs = if (t(8) == 1 && t(35) == 3) 1 else 0
      val cyjjs = if (t(30) == "1" && t(31) == "1" && t(39) == "1" && t(2)!= 1) 1 else 0
      val jjcgs = if (t(30) == "1" && t(31) == "1" && t(42) == "1") 1 else 0
      val zss = if (t(8) == 2 && t(30) == "1") 1 else 0
      val djs = if (t(8) == 3 && t(30) == "1") 1 else 0
      val ggxf = if (t(30) == "1" && t(31) == "1" && t(42) == "1") t(75).toString.toDouble/1000 else 0
      val ggcb = if (t(30) == "1" && t(31) == "1" && t(42) == "1") t(41).toString.toDouble/1000 else 0
      ((t(24),t(25)),(ysqqs,yxqqs,ggqqs,cyjjs,jjcgs,zss,djs,ggxf,ggcb))
    })
    val reducedProvAndCity = provAndCityAndParas.reduceByKey((x,y)=>(x._1+y._1,x._2+y._2,x._3+y._3,x._4+y._4,x._5+y._5,x._6+y._6,x._7+y._7,x._8+y._8,x._9+y._9))
    // 省 市 1原始请求数 2有效请求书 3广告请求数 4参与竞价数 5竞价成功数 竞价成功率 6展示数 7点击量 点击率 8广告成本 9广告消费
    val result = reducedProvAndCity.map(t=>Row(t._1._1, t._1._2, t._2._1,t._2._2,t._2._3,t._2._4,t._2._5,t._2._6,t._2._7,t._2._8,t._2._9))

    println(result.collect().toList)
    val sch: StructType = StructType(List(
      StructField("省", StringType, true),
      StructField("市", StringType, true),
      StructField("原始请求数", IntegerType, true),
      StructField("有效请求数", IntegerType, true),
      StructField("广告请求数", IntegerType, true),
      StructField("参与竞价数", IntegerType, true),
      StructField("竞价成功数", IntegerType, true),
      StructField("展示数", IntegerType, true),
      StructField("点击量", IntegerType, true),
      StructField("广告成本", DoubleType, true),
      StructField("广告消费", DoubleType, true)
    ))

    val resultDF: DataFrame = sqlContext.createDataFrame(result,sch)

    // 以parquet类型写入磁盘
    resultDF.write.parquet("E:/mrdata/ad/logs/provcityarea")
    resultDF.write.json("E:/mrdata/ad/logs/json")

    // 使用jdbc写入mysql数据库
        val props = new Properties()
        props.put("user","root")
        props.put("password","123456")
        resultDF.write.mode("append").jdbc("jdbc:mysql://localhost:3306/sparkad?characterEncoding=UTF-8", "prov_city_area2", props)
  }
}
