//package students.yunanbing
//
//import org.apache.spark.rdd.RDD
//import org.apache.spark.{SparkConf, SparkContext}
//
//import scala.collection.mutable.ArrayBuffer
//
//object Index2 {
//  //使用spark算子计算指标
//  def main(args: Array[String]): Unit = {
//
//    // 1.参数设置
//    if (args.length != 1) {
//      println(
//        """
//          |参数有两个
//          |都要填写
//        """.stripMargin)
//      return
//    }
//    val Array(inputpath) = args
//
//    val conf: SparkConf = new SparkConf().setAppName("Index2").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//    val sc: SparkContext = new SparkContext(conf)
//    val file: RDD[String] = sc.textFile(inputpath)
//  //取字段
//    val origianl: RDD[(String, Int, Int,Int)] = file.map(
//      lines => {
//        var t = ("", 0,0,0)
//        val line: Array[String] = lines.split(",",-1)
//        val f: Array[String] = line.filter(_.length==85)
//        val provincename: String = f(24)
//        val cityname: String = f(25)
//        val requestmode: Int = FileUtils.toInt(f(8))
//        val processnode: Int = FileUtils.toInt(f(35))
//        t= (cityname, requestmode, processnode,1)
//        t
//      }).cache()
//
//
//
//    val request: RDD[(String, Int, Int, Int)] = origianl.filter(_._2==1).filter(_._3>=1)
//    val citydata: RDD[(String, Int)] = request.map(t => (t._1, t._4))
//    val datasum: RDD[(String, Int)] = citydata.reduceByKey(_+_)
//    val collect: Array[(String, Int)] = datasum.collect()
//    //println(collect.toBuffer)
//
//    val effective: RDD[(String, Int, Int, Int)] = origianl.filter(_._2==1).filter(_._3>=2)
//    val citydata2: RDD[(String, Int)] = effective.map(t => (t._1, t._4))
//    val datasum2: RDD[(String, Int)] = citydata2.reduceByKey(_+_)
//    val collect2: Array[(String, Int)] = datasum2.collect()
//    //println(collect2.toBuffer)
//
//
//    var arr = ArrayBuffer[(String,Int,Int)]()
//    for(t <- collect){
//      for(f <- collect2){
//        if(f._1.equals(t._1)){
//          var n= (t._1,t._2,f._2)
//          arr += n
//        }
//      }
//    }
//
//
//  }
//}
