//package students.zhangxian
//
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.types._
//import org.apache.spark.sql.{DataFrame, Row, SQLContext}
//import org.apache.spark.{SparkConf, SparkContext}
//
///**
//  * 处理日志文件，写入磁盘为parquet类型
//  * 读取数据为RDD，转换为RDD[Row]类型，再转换为DataFrame，以parquet写入磁盘
//  * text=>RDD[String]=>RDD[Row}=>DataFrame=>parquet
//  */
//object log2ParquetRow {
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("logToParquet").setMaster("local[*]")
//    // 写parquet文件，压缩方式使用snappy
//    conf.set("spark.sql.parquet.compression.codec", "snappy")
//    // 序列化 用于shuffing和RDD写入磁盘的序列化。默认的是serializable,这里是指定kryo。效率快十倍
//    // 自定义的类，要用kryo的话，需要注册
//    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    conf.registerKryoClasses(Array(Utils.getClass))
//
//    val sc = new SparkContext(conf)
//    val sqlContext = new SQLContext(sc)
//
//    // 读取文件为RDD
//    val lines: RDD[String] = sc.textFile("E:/mrdata/ad/logs/input")
//    // 切分处理，过滤脏数据，然后处理为RDD[Row]
//    val rowRDD: RDD[Row] = lines.map(_.split(",")).filter(_.length == 85).map(Utils.arrayToRow(_))
//    // 创建schame信息
//    val sch: StructType = StructType(Utils.structTypeList)
//    // 用RDD[Row]和schame创建DataFrame
//    val bdf: DataFrame = sqlContext.createDataFrame(rowRDD, sch)
//
//    //bdf.show()
//    // 以parquet类型写入磁盘
//    bdf.write.parquet("E:/mrdata/ad/logs/output")
//  }
//}
