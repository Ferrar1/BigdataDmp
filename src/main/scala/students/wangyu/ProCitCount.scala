package students.wangyu

import java.util.Properties

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}

object ProCitCount {
  def main(args: Array[String]): Unit = {

    //创建SparkConf配置文件
    val conf: SparkConf = new SparkConf().setAppName("ProCitCount").setMaster("local[*]")
    //建一个SparkContext
    val sc = new SparkContext(conf)
    //建SQLContext
    val sqlContext = new SQLContext(sc)
    //读取文件
    val linesRDD: RDD[String] = sc.textFile(args(0))
    //数据清洗
    val filterRDD: RDD[Array[String]] = linesRDD.map(_.split(",",-1)).filter(_.length>=85)
    val provinceAndCity: RDD[(String, String)] = filterRDD.map(t => {
      (t(24), t(25))
    })
    val proAndCityDF: DataFrame = sqlContext.createDataFrame(provinceAndCity).toDF("provincename","cityname")
    proAndCityDF.registerTempTable("t_proAndCity")
    val result: DataFrame = sqlContext.sql("select provincename,cityname,count(1) ct from t_proAndCity group by provincename,cityname")
    val props = new Properties()
    props.put("user","root")
    props.put("password","123456")
    result.write.mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/log?characterEncoding=UTF-8","log_2",props)
    sc.stop()

  }

}
