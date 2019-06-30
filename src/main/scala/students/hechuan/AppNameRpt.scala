//package students.hechuan
//
//import java.util.Properties
//
//import org.apache.spark.broadcast.Broadcast
//import org.apache.spark.sql.{DataFrame, SQLContext}
//import org.apache.spark.{SparkConf, SparkContext}
//
//import scala.collection.mutable
//
//object AppNameRpt {
//
//  def main(args: Array[String]): Unit = {
//
//    if (args.length != 2) {
//      println(
//        """
//          |datacalculate.AppNameRpt
//          |you need input parameter
//          | inPutPath
//          | dictPath
//        """.stripMargin)
//      sys.exit()
//    }
//    //创建sparkConf
//    val conf = new SparkConf()
//      .setAppName(this.getClass.getSimpleName)
//      .setMaster("local[*]")
//      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//
//    //接受参数
//    val Array(inPutPath, dictPath) = args
//    //创建sparkContext
//    val sc = new SparkContext(conf)
//    //创建SqlContext
//    val sqlContext = new SQLContext(sc)
//    //获取需要广播的数据
//    val data = RptUtil.ReadDictData(dictPath)
//    //将数据广播出去
//    val broadcastRef: Broadcast[mutable.HashMap[String, String]] = sc.broadcast(data)
//
//    //读取数据
//    val dataRDD = sqlContext.read.parquet(inPutPath)
//
//    //注册临时表
//    dataRDD.registerTempTable("t_log")
//
//    val sql = sqlContext.sql("SELECT appname FROM t_log GROUP BY appname")
//
//
//    val appId2Name = (ipId: String) => {
//      val data = broadcastRef.value
//      data.getOrElseUpdate(ipId, "未知")
//    }
//    //注册udf
//    sqlContext.udf.register("getAppName", appId2Name)
//
//    val appNameDF: DataFrame = sqlContext.sql("""SELECT
//                                                |if(appname='',getAppName(appid),appname) as appname,
//                                                |requestmode,processnode,iseffective,isbilling,isbid,adorderid,iswin,winprice,adpayment
//                                                |FROM t_log""".stripMargin)
//   appNameDF.registerTempTable("t_full_data")
//   val prop = new Properties()
//    prop.load(this.getClass.getClassLoader.getResourceAsStream("jdbc.properties"))
//    sql.write.mode("overwrite").jdbc(prop.getProperty("url"),"t_appname",prop)
//
//    val mediaRpt = sqlContext.sql("""	SELECT
//                                    |	appname,
//                                    |	SUM(case when requestmode=1 AND processnode >=1 THEN 1 ELSE 0 END) primitive_request,
//                                    |	SUM(case when requestmode=1 AND processnode >=2 THEN 1 ELSE 0 END) effective_request,
//                                    |	SUM(case when requestmode=1 AND processnode =3 THEN 1 ELSE 0 END) ad_request,
//                                    |	SUM(case when iseffective=1 AND isbilling =1 AND isbid=1 AND adorderid != 0 THEN 1 ELSE 0 END) pcp_bidding,
//                                    |	SUM(case when iseffective=1 AND isbilling =1 AND iswin=1 THEN 1 ELSE 0 END) bidding_success,
//                                    |	SUM(case when requestmode=2 AND iseffective =1 THEN 1 ELSE 0 END) impression,
//                                    |	SUM(case when requestmode=3 AND iseffective =1 THEN 1 ELSE 0 END) clicks,
//                                    |	SUM(case when iseffective=1 AND isbilling =1 AND iswin=1 THEN winprice ELSE 0 END) dsp_adConsumer,
//                                    |	SUM(case when requestmode=1 AND processnode >=1 THEN adpayment ELSE 0 END) dsp_adCost
//                                    |	FROM t_full_data
//                                    |	GROUP BY appname""".stripMargin)
//
//      mediaRpt.write.mode("overwrite").jdbc(prop.getProperty("url"),"t_full_data",prop)
//
//      mediaRpt.show()
//
//    sc.stop()
//
//  }
//
//}
