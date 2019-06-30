//package students.jialiang
//
//import java.text.SimpleDateFormat
//
//import com.google.gson.Gson
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.{DataFrameReader, Row, SQLContext}
//import org.apache.spark.{SparkConf, SparkContext}
//
//object Bz2ToParquet {
//
//  def srr2obj(log:String): ZhuanHuan = {
//    new ZhuanHuan().bz2par(log)
//  }
//
//  def obj2Json(zhuanHuan: ZhuanHuan): String = {
//    val gson =new Gson()
//    gson.toJson(zhuanHuan)
//  }
//
//  def main(args: Array[String]): Unit = {
//      val conf = new SparkConf().setMaster("local[*]").setAppName("Bz2ToParquet").set("spark.serializer","org.apache.spark.serializer.KryoSerializer").set("spark.io.compression.codec","org.apache.spark.io.SnappyCompressionCodec")
//    val sc = new SparkContext(conf)
//    val slc = new  SQLContext(sc)
//    val dateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss")
//
//
//    import slc.implicits._
//    //读取数据
//    val lines = sc.textFile("/home/hanbing/Desktop/2016-10-01_06_p1_invalid.1475274123982.log.FINISH.bz2")
//    //将数据进行过滤并转化成对象
//    val obj = lines.map(srr2obj)
//
//    //过滤怒符合要求的数据并将数据转换成json
//    val json = obj.filter(_ != null).map(obj2Json)
//    //将数据以parquet存储
//    slc.read.json(json).write.parquet("/home/hanbing/Desktop/456")
//
//    if (slc.read.parquet("/home/hanbing/Desktop/456") !=null ||slc.read.parquet("/home/hanbing/Desktop/456")!= ""){
//      println("日志转换完成！")
//    }else{
//      println("未读取到日志信息，或转换失败")
//    }
//    sc.stop()
//
//    //reader.write.parquet("/home/hanbing/pj2logs/")
//
//  }
//}
