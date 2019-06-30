package students.cuibin

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.types.StructType
import org.apache.spark.{SparkConf, SparkContext}


object AdvertisingSql {
  def main(args: Array[String]): Unit = {
    //提交的这个程序可以连接到Spark集群中
    val conf: SparkConf = new SparkConf().setAppName("AdvertisingSql").setMaster("local[2]")
    //压缩编解码器设置为snappy
    conf.set("spark.sql.parquet.compression.codec", "snappy")
    //序列化方法采用KryoSerializer
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerialization")
    conf.registerKryoClasses(Array(LogToParguet.getClass))
    val sc: SparkContext = new SparkContext(conf)
    //创建SparkSQL的连接（程序执行的入口）
    val sqlContext = new SQLContext(sc)
    //设置文件的采集地址
    val lines: RDD[String] = sc.textFile(LogToParguet.filename)
    //清洗数据
    val line: RDD[Array[String]] = lines.map(_.split(",")).filter(_.length == 85)
    val rowRdd = line.map(line1 => {

      LogToParguet.arrToRow(line1)
    })

    //将RowRDD关联schema
    val schema: StructType = StructType(LogToParguet.schemaList)
    val cd: DataFrame = sqlContext.createDataFrame(rowRdd, schema)

    //设置文件的输出地址
    //cd.write.parquet(LogToParguet.forname)
    cd.show()
    sc.stop()
  }
}
